package com.stavro_xhardha.pockettreasure.ui.home

import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.model.HomePrayerTime
import com.stavro_xhardha.pockettreasure.model.PrayerTimeResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.joda.time.LocalTime


class HomeViewModel @AssistedInject constructor(
    private val homeRepository: HomeRepository,
    @Assisted val savedStateHandle: SavedStateHandle
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): HomeViewModel
    }

    private val exceptionHandle = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
        decrementIdlingResource()
        showError()
    }

    private val _progressBarVisibility = MutableLiveData<Int>()
    private val _showErrorToast = MutableLiveData<Boolean>()
    private val _contentVisibility = MutableLiveData<Int>()
    private val _workManagerHasBeenFired = MutableLiveData<Boolean>()

    val progressBarVisibility: LiveData<Int> = _progressBarVisibility
    val showErrorToast: LiveData<Boolean> = _showErrorToast
    val contentVisibility: LiveData<Int> = _contentVisibility
    val homeData: LiveData<ArrayList<HomePrayerTime>> = savedStateHandle.getLiveData(HOME_DATA_LIST)

    val workManagerHasBeenFired: LiveData<Boolean> = _workManagerHasBeenFired

    fun loadPrayerTimes() {
        viewModelScope.launch(Dispatchers.Main + exceptionHandle) {
            val dateHasPassed = dateHasPassed()
            val countryHasBeenUpdated = homeRepository.countryHasBeenUpdated()
            if (dateHasPassed || countryHasBeenUpdated) {
                makePrayerApiCall()
            } else {
                withContext(Dispatchers.Main) {
                    setValuesToLiveData()
                }
            }
        }
    }

    private suspend fun makePrayerApiCall() {
        switchProgressBarOn()
        incrementIdlingResource()
        withContext(Dispatchers.IO) {
            val todaysPrayerTime = homeRepository.makePrayerCallAsync()
            if (todaysPrayerTime.isSuccessful) {
                decrementIdlingResource()
                saveDataToShardPreferences(todaysPrayerTime.body())
                withContext(Dispatchers.Main) {
                    setValuesToLiveData()
                }
            } else {
                withContext(Dispatchers.Main) {
                    decrementIdlingResource()
                }
            }
        }
    }

    private suspend fun setValuesToLiveData() {
        val homePrayerData = homeRepository.getHomeData()
        findCurrentTime(homePrayerData)
        switchProgressBarOff()
        checkWorkManager()
    }

    private suspend fun checkWorkManager() {
        val isWorkerFired = homeRepository.isWorkerFired()
        _workManagerHasBeenFired.postValue(isWorkerFired)
    }

    private fun findCurrentTime(homePrayerData: ArrayList<HomePrayerTime>) {
        val currentTime = LocalTime()
        if (currentTime.isBefore(localTime(homePrayerData[0].time)) ||
            currentTime.isAfter(localTime(homePrayerData[4].time))
        ) {
            homePrayerData[0].backgroundColor =
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) DARK_SELECTOR else LIGHT_SELECTOR
            checkOtherColors(homePrayerData)
        } else {
            var currentColorFound = false
            for (i in 0 until homePrayerData.size) {
                if (currentTime.isBefore(localTime(homePrayerData[i].time)) && !currentColorFound) {
                    homePrayerData[i].backgroundColor =
                        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) DARK_SELECTOR else LIGHT_SELECTOR
                    currentColorFound = true
                } else {
                    homePrayerData[i].backgroundColor = TRANSPARENT
                }
            }
        }

        savedStateHandle.set(HOME_DATA_LIST, homePrayerData)
    }

    private fun checkOtherColors(homePrayerData: ArrayList<HomePrayerTime>) {
        for (i in 1 until homePrayerData.size) {
            homePrayerData[i].backgroundColor = TRANSPARENT
        }
    }

    private fun localTime(timeOfPrayer: String): LocalTime = LocalTime(
        (timeOfPrayer.substring(0, 2).toInt()),
        (timeOfPrayer.substring(3, 5)).toInt()
    )

    private suspend fun dateHasPassed(): Boolean {
        val date = DateTime()
        return !(date.dayOfMonth == homeRepository.getCurrentRegisteredDay() &&
                date.monthOfYear == homeRepository.getCurrentRegisteredMonth() &&
                date.year == homeRepository.getCurrentRegisteredYear())
    }

    private suspend fun saveDataToShardPreferences(prayerTimeResponse: PrayerTimeResponse?) {
        if (prayerTimeResponse != null) {
            try {
                saveThePrayerTimeResponseToMemory(prayerTimeResponse)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun saveThePrayerTimeResponseToMemory(prayerTimeResponse: PrayerTimeResponse) {
        prayerTimeResponse.let {
            homeRepository.saveFajrTime(it.data.timings.fajr)
            homeRepository.saveFinishFajrTime(it.data.timings.sunrise)
            homeRepository.saveDhuhrTime(it.data.timings.dhuhr)
            homeRepository.saveAsrTime(it.data.timings.asr)
            homeRepository.saveMagribTime(it.data.timings.magrib)
            homeRepository.saveIshaTime(it.data.timings.isha)
            homeRepository.saveDayOfMonth(it.data.date.gregorianDate.day.toInt())
            homeRepository.saveYear(it.data.date.gregorianDate.year.toInt())
            homeRepository.saveMonthOfYear(it.data.date.gregorianDate.gregorianMonth.number)
            homeRepository.saveMonthName(it.data.date.gregorianDate.gregorianMonth.monthNameInEnglish)
            homeRepository.saveDayOfMonthHijri(it.data.date.hijriPrayerDate.day)
            homeRepository.saveMonthOfYearHijri(it.data.date.hijriPrayerDate.hirjiMonth.monthNameInEnglish)
            homeRepository.saveYearHijri(it.data.date.hijriPrayerDate.year)
            homeRepository.saveMidnight(it.data.timings.midnight)
            homeRepository.updateCountryState()
        }
    }

    fun updateWorkManagerFiredState() {
        viewModelScope.launch {
            homeRepository.updateWorkerFired()
        }
    }

    private fun showError() {
        _showErrorToast.value = true
        _progressBarVisibility.value = View.GONE
        _contentVisibility.value = View.VISIBLE
    }

    private fun switchProgressBarOn() {
        _progressBarVisibility.value = View.VISIBLE
        _contentVisibility.value = View.GONE
        _showErrorToast.value = false
    }

    private fun switchProgressBarOff() {
        _progressBarVisibility.value = View.GONE
        _contentVisibility.value = View.VISIBLE
        _showErrorToast.value = false
    }
}