package com.stavro_xhardha.pockettreasure.ui.settings

import android.location.Geocoder
import androidx.lifecycle.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.stavro_xhardha.pockettreasure.brain.decrementIdlingResource
import com.stavro_xhardha.pockettreasure.brain.incrementIdlingResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class SettingsViewModel @AssistedInject constructor(
    private val settingsRepository: SettingsRepository,
    @Assisted val savedStateHandle: SavedStateHandle
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): SettingsViewModel
    }

    private val _fajrCheck: MutableLiveData<Boolean> = MutableLiveData()
    private val _dhuhrCheck: MutableLiveData<Boolean> = MutableLiveData()
    private val _asrCheck: MutableLiveData<Boolean> = MutableLiveData()
    private val _maghribCheck: MutableLiveData<Boolean> = MutableLiveData()
    private val _ishaCheck: MutableLiveData<Boolean> = MutableLiveData()
    private val _countryAndCapital: MutableLiveData<String> = MutableLiveData()
    private val _workManagerReadyToStart = MutableLiveData<Boolean>()
    private val _locationRequestTurnOff = MutableLiveData<Boolean>()
    private val _serviceNotAvailableVisibility = MutableLiveData<Boolean>()
    private val _locationErrorVisibility = MutableLiveData<Boolean>()

    val locationRequestTurnOff: LiveData<Boolean> = _locationRequestTurnOff
    val serviceNotAvailableVisibility: LiveData<Boolean> = _serviceNotAvailableVisibility
    val locationerrorVisibility: LiveData<Boolean> = _locationErrorVisibility
    private var locationTurnOfRequested: Boolean = false

    val fajrCheck: LiveData<Boolean> = _fajrCheck
    val dhuhrCheck: LiveData<Boolean> = _dhuhrCheck
    val asrCheck: LiveData<Boolean> = _asrCheck
    val maghribCheck: LiveData<Boolean> = _maghribCheck
    val ishaCheck: LiveData<Boolean> = _ishaCheck
    val countryAndCapital: LiveData<String> = _countryAndCapital
    val workManagerReadyToStart: LiveData<Boolean> = _workManagerReadyToStart

    private var fajrCheckHelper: Boolean = false
    private var dhuhrCheckHelper: Boolean = false
    private var asrCheckCheckHelper: Boolean = false
    private var mahgribCheckCheckHelper: Boolean = false
    private var ishaCheckCheckHelper: Boolean = false
    private var workerReadyToRestart = false

    init {
        listenToRepository()
    }

    private fun listenToRepository() {
        incrementIdlingResource()
        viewModelScope.launch {
            settingsRepository.let {
                fajrCheckHelper = it.getFajrChecked()
                dhuhrCheckHelper = it.getDhuhrChecked()
                asrCheckCheckHelper = it.getAsrChecked()
                mahgribCheckCheckHelper = it.getMaghribChecked()
                ishaCheckCheckHelper = it.getIshaChecked()
            }
            withContext(Dispatchers.Main) {
                this@SettingsViewModel._fajrCheck.value = fajrCheckHelper
                this@SettingsViewModel._dhuhrCheck.value = dhuhrCheckHelper
                this@SettingsViewModel._asrCheck.value = asrCheckCheckHelper
                this@SettingsViewModel._maghribCheck.value = mahgribCheckCheckHelper
                this@SettingsViewModel._ishaCheck.value = ishaCheckCheckHelper
                this@SettingsViewModel._countryAndCapital.value = settingsRepository.readCountryAndCapital()
                decrementIdlingResource()
            }
        }
    }

    fun onSwFajrClick(checked: Boolean) {
        incrementIdlingResource()
        viewModelScope.launch {
            settingsRepository.putFajrNotification(checked)
            withContext(Dispatchers.Main) {
                decrementIdlingResource()
                _fajrCheck.value = settingsRepository.getFajrChecked()
            }
        }
    }

    fun onSwDhuhrClick(checked: Boolean) {
        incrementIdlingResource()
        viewModelScope.launch {
            settingsRepository.putDhuhrNotification(checked)
            withContext(Dispatchers.Main) {
                decrementIdlingResource()
                _dhuhrCheck.value = settingsRepository.getDhuhrChecked()
            }
        }
    }

    fun onSwAsrClick(checked: Boolean) {
        incrementIdlingResource()
        viewModelScope.launch {
            settingsRepository.putAsrNotification(checked)
            withContext(Dispatchers.Main) {
                decrementIdlingResource()
                _asrCheck.value = settingsRepository.getAsrChecked()
            }
        }
    }

    fun onSwMaghribClick(checked: Boolean) {
        incrementIdlingResource()
        viewModelScope.launch {
            settingsRepository.putMaghribNotification(checked)
            withContext(Dispatchers.Main) {
                decrementIdlingResource()
                _maghribCheck.value = settingsRepository.getMaghribChecked()
            }
        }
    }

    fun onSwIshaClick(checked: Boolean) {
        incrementIdlingResource()
        viewModelScope.launch {
            settingsRepository.putIshaNotification(checked)
            withContext(Dispatchers.Main) {
                decrementIdlingResource()
                _ishaCheck.value = settingsRepository.getIshaChecked()
            }
        }
    }

    private fun resetDataForWorker() {
        viewModelScope.launch {
            settingsRepository.deleteAllDataInside()
            _workManagerReadyToStart.postValue(true)
        }
    }

    fun convertToAdress(geocoder: Geocoder, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val adresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (adresses.size != 0) {
                    val cityName = adresses[0].adminArea
                    val country = adresses[0].countryName
                    settingsRepository.updateCountryAndLocation(country, cityName, latitude, longitude)
                    if (!locationTurnOfRequested) {
                        locationTurnOfRequested = true
                    }
                }
                workerReadyToRestart = true
            } catch (exception: IOException) {
                exception.printStackTrace()
                _serviceNotAvailableVisibility.postValue(true)
                workerReadyToRestart = false
            } catch (illegalArgumentException: IllegalArgumentException) {
                illegalArgumentException.printStackTrace()
                _locationErrorVisibility.postValue(true)
                workerReadyToRestart = false
            } finally {
                _locationRequestTurnOff.postValue(true)
                if (workerReadyToRestart)
                    resetDataForWorker()
            }
        }
    }
}