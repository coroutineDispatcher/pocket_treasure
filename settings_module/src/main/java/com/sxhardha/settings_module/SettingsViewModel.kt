package com.sxhardha.settings_module

import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stavro_xhardha.core_module.core_dependencies.AppCoroutineDispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _fajrCheck: MutableLiveData<Boolean> = MutableLiveData()
    private val _dhuhrCheck: MutableLiveData<Boolean> = MutableLiveData()
    private val _asrCheck: MutableLiveData<Boolean> = MutableLiveData()
    private val _maghribCheck: MutableLiveData<Boolean> = MutableLiveData()
    private val _ishaCheck: MutableLiveData<Boolean> = MutableLiveData()
    private val _countryAndCapital: MutableLiveData<String> = MutableLiveData()
    private val _locationRequestTurnOff = MutableLiveData<Boolean>()
    private val _serviceNotAvailableVisibility = MutableLiveData<Boolean>()
    private val _locationErrorVisibility = MutableLiveData<Boolean>()

    val locationRequestTurnOff: LiveData<Boolean> = _locationRequestTurnOff
    val serviceNotAvailableVisibility: LiveData<Boolean> = _serviceNotAvailableVisibility
    val locationerrorVisibility: LiveData<Boolean> = _locationErrorVisibility
    val fajrCheck: LiveData<Boolean> = _fajrCheck
    val dhuhrCheck: LiveData<Boolean> = _dhuhrCheck
    val asrCheck: LiveData<Boolean> = _asrCheck
    val maghribCheck: LiveData<Boolean> = _maghribCheck
    val ishaCheck: LiveData<Boolean> = _ishaCheck
    val countryAndCapital: LiveData<String> = _countryAndCapital

    private var fajrCheckHelper: Boolean = false
    private var dhuhrCheckHelper: Boolean = false
    private var asrCheckCheckHelper: Boolean = false
    private var mahgribCheckCheckHelper: Boolean = false
    private var ishaCheckCheckHelper: Boolean = false
    private var locationTurnOfRequested: Boolean = false

    init {
        listenToRepository()
    }

    private fun listenToRepository() {
        viewModelScope.launch(appCoroutineDispatchers.ioDispatcher) {
            settingsRepository.let {
                fajrCheckHelper = it.getFajrChecked()
                dhuhrCheckHelper = it.getDhuhrChecked()
                asrCheckCheckHelper = it.getAsrChecked()
                mahgribCheckCheckHelper = it.getMaghribChecked()
                ishaCheckCheckHelper = it.getIshaChecked()
            }
            withContext(appCoroutineDispatchers.mainDispatcher) {
                this@SettingsViewModel._fajrCheck.value = fajrCheckHelper
                this@SettingsViewModel._dhuhrCheck.value = dhuhrCheckHelper
                this@SettingsViewModel._asrCheck.value = asrCheckCheckHelper
                this@SettingsViewModel._maghribCheck.value = mahgribCheckCheckHelper
                this@SettingsViewModel._ishaCheck.value = ishaCheckCheckHelper
                this@SettingsViewModel._countryAndCapital.value =
                    settingsRepository.readCountryAndCapital()
            }
        }
    }

    fun onSwFajrClick(checked: Boolean) {
        viewModelScope.launch(appCoroutineDispatchers.ioDispatcher) {
            settingsRepository.putFajrNotification(checked)
            _fajrCheck.postValue(settingsRepository.getFajrChecked())
        }
    }

    fun onSwDhuhrClick(checked: Boolean) {
        viewModelScope.launch(appCoroutineDispatchers.ioDispatcher) {
            settingsRepository.putDhuhrNotification(checked)
            _dhuhrCheck.postValue(settingsRepository.getDhuhrChecked())
        }
    }

    fun onSwAsrClick(checked: Boolean) {
        viewModelScope.launch(appCoroutineDispatchers.ioDispatcher) {
            settingsRepository.putAsrNotification(checked)
            _asrCheck.postValue(settingsRepository.getAsrChecked())
        }
    }

    fun onSwMaghribClick(checked: Boolean) {
        viewModelScope.launch(appCoroutineDispatchers.ioDispatcher) {
            settingsRepository.putMaghribNotification(checked)
            _maghribCheck.postValue(settingsRepository.getMaghribChecked())
        }
    }

    fun onSwIshaClick(checked: Boolean) {
        viewModelScope.launch(appCoroutineDispatchers.ioDispatcher) {
            settingsRepository.putIshaNotification(checked)
            _ishaCheck.postValue(settingsRepository.getIshaChecked())
        }
    }

    fun convertToAdress(geocoder: Geocoder, latitude: Double, longitude: Double) {
        viewModelScope.launch(appCoroutineDispatchers.ioDispatcher) {
            try {
                _countryAndCapital.postValue("...")
                val adresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (adresses.size != 0) {
                    val cityName = adresses[0].adminArea
                    val country = adresses[0].countryName
                    settingsRepository.updateCountryAndLocation(
                        country,
                        cityName,
                        latitude,
                        longitude
                    )
                    if (!locationTurnOfRequested) {
                        locationTurnOfRequested = true
                    }
                }
            } catch (exception: IOException) {
                exception.printStackTrace()
                _serviceNotAvailableVisibility.postValue(true)
            } catch (illegalArgumentException: IllegalArgumentException) {
                illegalArgumentException.printStackTrace()
                _locationErrorVisibility.postValue(true)
            } finally {
                _locationRequestTurnOff.postValue(true)
                _countryAndCapital.postValue(settingsRepository.readCountryAndCapital())
            }
        }
    }
}