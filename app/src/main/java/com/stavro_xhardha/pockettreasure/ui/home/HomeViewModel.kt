package com.stavro_xhardha.pockettreasure.ui.home

import android.view.View
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

    val progressBarVisibility: LiveData<Int> = _progressBarVisibility
    val showErrorToast: LiveData<Boolean> = _showErrorToast
    val contentVisibility: LiveData<Int> = _contentVisibility
    val homeData: LiveData<ArrayList<HomePrayerTime>> = savedStateHandle.getLiveData(HOME_DATA_LIST)

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
    }

    private fun findCurrentTime(homePrayerData: ArrayList<HomePrayerTime>) {
        val currentTime = LocalTime()
        if (currentTime.isBefore(localTime(homePrayerData[0].time)) ||
            currentTime.isAfter(localTime(homePrayerData[4].time))
        ) {
            homePrayerData[0].backgroundColor = SELECTOR
            checkOtherColors(homePrayerData)
        } else {
            var currentColorFound = false
            for (i in 0 until homePrayerData.size) {
                if (currentTime.isBefore(localTime(homePrayerData[i].time)) && !currentColorFound) {
                    homePrayerData[i].backgroundColor = SELECTOR
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
            homeRepository.saveFajrTime(it.data.timings.Fajr)
            homeRepository.saveFinishFajrTime(it.data.timings.Sunrise)
            homeRepository.saveDhuhrTime(it.data.timings.Dhuhr)
            homeRepository.saveAsrTime(it.data.timings.Asr)
            homeRepository.saveMagribTime(it.data.timings.Maghrib)
            homeRepository.saveIshaTime(it.data.timings.Isha)
            homeRepository.saveDayOfMonth(it.data.date.gregorian.day.toInt())
            homeRepository.saveYear(it.data.date.gregorian.year.toInt())
            homeRepository.saveMonthOfYear(it.data.date.gregorian.month.number)
            homeRepository.saveMonthName(it.data.date.gregorian.month.en)
            homeRepository.saveDayOfMonthHijri(it.data.date.hijri.day)
            homeRepository.saveMonthOfYearHijri(it.data.date.hijri.month.en)
            homeRepository.saveYearHijri(it.data.date.hijri.year)
            homeRepository.saveMidnight(it.data.timings.Midnight)
            homeRepository.updateCountryState()
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