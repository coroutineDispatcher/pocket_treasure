package com.stavro_xhardha.pockettreasure.ui.home

import android.view.View
import androidx.lifecycle.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.stavro_xhardha.pockettreasure.brain.ACCENT_BACKGROUND
import com.stavro_xhardha.pockettreasure.brain.WHITE_BACKGROUND
import com.stavro_xhardha.pockettreasure.brain.decrementIdlingResource
import com.stavro_xhardha.pockettreasure.brain.incrementIdlingResource
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

    private val exceptionHandle = CoroutineExceptionHandler { _, _ ->
        decrementIdlingResource()
        showError()
    }

    val monthSection = MutableLiveData<String>()
    val locationSecton = MutableLiveData<String>()
    val fajrTime = MutableLiveData<String>()
    val dhuhrtime = MutableLiveData<String>()
    val asrTime = MutableLiveData<String>()
    val maghribTime = MutableLiveData<String>()
    val ishaTime = MutableLiveData<String>()
    val progressBarVisibility = MutableLiveData<Int>()
    val showErroToast = MutableLiveData<Boolean>()
    val contentVisibility = MutableLiveData<Int>()
    val fajrColor = MutableLiveData<Int>()
    val dhuhrColor = MutableLiveData<Int>()
    val asrColor = MutableLiveData<Int>()
    val maghribColor = MutableLiveData<Int>()
    val ishaColor = MutableLiveData<Int>()
    private val _workManagerHasBeenFired = MutableLiveData<Boolean>()

    val workManagerHasBeenFired: LiveData<Boolean> = _workManagerHasBeenFired

    fun loadPrayerTimes() {
        viewModelScope.launch(Dispatchers.Main + exceptionHandle) {
            if (dateHasPassed() || homeRepository.countryHasBeenUpdated()) {
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
                    showError()
                }
            }
        }
    }

    private fun showError() {
        showErroToast.value = true
        progressBarVisibility.value = View.GONE
        contentVisibility.value = View.VISIBLE
    }

    private fun switchProgressBarOn() {
        progressBarVisibility.value = View.VISIBLE
        contentVisibility.value = View.GONE
        showErroToast.value = false
    }

    private fun switchProgressBarOff() {
        progressBarVisibility.value = View.GONE
        contentVisibility.value = View.VISIBLE
        showErroToast.value = false
    }

    private suspend fun setValuesToLiveData() {
        putValues()
        findCurrentTime()
        switchProgressBarOff()
    }

    private suspend fun putValues() {
        monthSection.postValue(homeRepository.readMonthSection())
        locationSecton.postValue(homeRepository.readLocationSection())
        fajrTime.postValue("${homeRepository.readFejrtime()} - ${homeRepository.readFinishFajrTime()}")
        dhuhrtime.postValue(homeRepository.readDhuhrTime())
        asrTime.postValue(homeRepository.readAsrTime())
        maghribTime.postValue(homeRepository.readMaghribTime())
        ishaTime.postValue(homeRepository.readIshaTime())
    }

    private suspend fun findCurrentTime() {
        val currentTime = LocalTime()
        try {
            compareTiming(
                currentTime,
                localTime(homeRepository.readFejrtime()!!),
                localTime(homeRepository.readDhuhrTime()!!),
                localTime(homeRepository.readAsrTime()!!),
                localTime(homeRepository.readMaghribTime()!!),
                localTime(homeRepository.readIshaTime()!!)
            )
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    private fun localTime(timeOfPrayer: String): LocalTime = LocalTime(
        (timeOfPrayer.substring(0, 2).toInt()),
        (timeOfPrayer.substring(3, 5)).toInt()
    )

    private fun compareTiming(
        currentTime: LocalTime,
        fajrTime: LocalTime,
        dhuhrTime: LocalTime,
        asrTime: LocalTime,
        maghribTime: LocalTime,
        ishaTime: LocalTime
    ) {
        when {
            currentTime.isBefore(fajrTime) -> switchFajrOn()
            currentTime.isBefore(dhuhrTime) -> switchDhuhrOn()
            currentTime.isBefore(asrTime) -> switchAsrOn()
            currentTime.isBefore(maghribTime) -> switchMaghribOn()
            currentTime.isBefore(ishaTime) -> switchIshaOn()
            else -> switchFajrOn()
        }
    }

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

    private fun switchFajrOn() {
        fajrColor.value = ACCENT_BACKGROUND
        dhuhrColor.value = WHITE_BACKGROUND
        asrColor.value = WHITE_BACKGROUND
        maghribColor.value = WHITE_BACKGROUND
        ishaColor.value = WHITE_BACKGROUND
    }

    private fun switchDhuhrOn() {
        fajrColor.value = WHITE_BACKGROUND
        dhuhrColor.value = ACCENT_BACKGROUND
        asrColor.value = WHITE_BACKGROUND
        maghribColor.value = WHITE_BACKGROUND
        ishaColor.value = WHITE_BACKGROUND
    }

    private fun switchAsrOn() {
        fajrColor.value = WHITE_BACKGROUND
        dhuhrColor.value = WHITE_BACKGROUND
        asrColor.value = ACCENT_BACKGROUND
        maghribColor.value = WHITE_BACKGROUND
        ishaColor.value = WHITE_BACKGROUND
    }

    private fun switchMaghribOn() {
        fajrColor.value = WHITE_BACKGROUND
        dhuhrColor.value = WHITE_BACKGROUND
        asrColor.value = WHITE_BACKGROUND
        maghribColor.value = ACCENT_BACKGROUND
        ishaColor.value = WHITE_BACKGROUND
    }

    private fun switchIshaOn() {
        fajrColor.value = WHITE_BACKGROUND
        dhuhrColor.value = WHITE_BACKGROUND
        asrColor.value = WHITE_BACKGROUND
        maghribColor.value = WHITE_BACKGROUND
        ishaColor.value = ACCENT_BACKGROUND
    }

    fun initWorker() {
        viewModelScope.launch {
            val isWorkerFired = homeRepository.isWorkerFired()
            _workManagerHasBeenFired.postValue(isWorkerFired)
        }
    }

    fun updateWorkManagerFiredState() {
        viewModelScope.launch {
            homeRepository.updateWorkerFired()
        }
    }
}