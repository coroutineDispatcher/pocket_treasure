package com.sxhardha.settings_module

import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stavro_xhardha.core_module.Event
import com.stavro_xhardha.core_module.brain.*
import com.stavro_xhardha.core_module.core_dependencies.AppCoroutineDispatchers
import com.stavro_xhardha.rocket.Rocket
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val rocket: Rocket
) : ViewModel() {

    private val _fajrCheck: MutableLiveData<Boolean> = MutableLiveData()
    private val _dhuhrCheck: MutableLiveData<Boolean> = MutableLiveData()
    private val _asrCheck: MutableLiveData<Boolean> = MutableLiveData()
    private val _maghribCheck: MutableLiveData<Boolean> = MutableLiveData()
    private val _ishaCheck: MutableLiveData<Boolean> = MutableLiveData()
    private val _countryAndCapital: MutableLiveData<String> = MutableLiveData()
    private val _locationRequestTurnOff = MutableLiveData<Event<Int>>()
    private val _serviceNotAvailableVisibility = MutableLiveData<Event<Int>>()
    private val _locationErrorVisibility = MutableLiveData<Event<Int>>()

    val locationRequestTurnOff: LiveData<Event<Int>> = _locationRequestTurnOff
    val serviceNotAvailableVisibility: LiveData<Event<Int>> = _serviceNotAvailableVisibility
    val locationerrorVisibility: LiveData<Event<Int>> = _locationErrorVisibility
    val fajrCheck: LiveData<Boolean> = _fajrCheck
    val dhuhrCheck: LiveData<Boolean> = _dhuhrCheck
    val asrCheck: LiveData<Boolean> = _asrCheck
    val maghribCheck: LiveData<Boolean> = _maghribCheck
    val ishaCheck: LiveData<Boolean> = _ishaCheck
    val countryAndCapital: LiveData<String> = _countryAndCapital

    private var locationTurnOfRequested: Boolean = false

    init {
        _fajrCheck.value = getFajrChecked()
        _dhuhrCheck.value = getDhuhrChecked()
        _asrCheck.value = getAsrChecked()
        _maghribCheck.value = getMaghribChecked()
        _ishaCheck.value = getIshaChecked()
        _countryAndCapital.value = readCountryAndCapital()
    }

    fun onSwFajrClick(checked: Boolean) {
        rocket.writeBoolean(NOTIFY_USER_FOR_FAJR, checked)
        _fajrCheck.postValue(getFajrChecked())
    }

    fun onSwDhuhrClick(checked: Boolean) {
        rocket.writeBoolean(NOTIFY_USER_FOR_DHUHR, checked)
        _dhuhrCheck.postValue(getDhuhrChecked())
    }

    fun onSwAsrClick(checked: Boolean) {
        rocket.writeBoolean(NOTIFY_USER_FOR_ASR, checked)
        _asrCheck.postValue(getAsrChecked())
    }

    fun onSwMaghribClick(checked: Boolean) {
        rocket.writeBoolean(NOTIFY_USER_FOR_MAGHRIB, checked)
        _maghribCheck.postValue(getMaghribChecked())
    }

    fun onSwIshaClick(checked: Boolean) {
        rocket.writeBoolean(NOTIFY_USER_FOR_ISHA, checked)
        _ishaCheck.postValue(getIshaChecked())
    }

    fun convertToAdress(geocoder: Geocoder, latitude: Double, longitude: Double) {
        viewModelScope.launch(appCoroutineDispatchers.ioDispatcher) {
            try {
                _countryAndCapital.postValue("...")
                val adresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (adresses.size != 0) {
                    val cityName = adresses[0].adminArea
                    val country = adresses[0].countryName
                    rocket.writeString(COUNTRY_SHARED_PREFERENCE_KEY, country)
                    rocket.writeString(CAPITAL_SHARED_PREFERENCES_KEY, cityName)
                    rocket.writeBoolean(COUNTRY_UPDATED, true)
                    rocket.writeFloat(LATITUDE_KEY, latitude.toFloat())
                    rocket.writeFloat(LONGITUDE_KEY, longitude.toFloat())
                    if (!locationTurnOfRequested) {
                        locationTurnOfRequested = true
                    }
                }
            } catch (exception: IOException) {
                exception.printStackTrace()
                _serviceNotAvailableVisibility.postValue(Event(R.string.service_not_available))
            } catch (illegalArgumentException: IllegalArgumentException) {
                illegalArgumentException.printStackTrace()
                _locationErrorVisibility.postValue(Event(R.string.invalid_coorinates))
            } finally {
                _locationRequestTurnOff.postValue(Event(R.string.location_updated_successfully))
                _countryAndCapital.postValue(readCountryAndCapital())
            }
        }
    }

    private fun getFajrChecked(): Boolean = rocket.readBoolean(NOTIFY_USER_FOR_FAJR)

    private fun getDhuhrChecked(): Boolean = rocket.readBoolean(NOTIFY_USER_FOR_DHUHR)

    private fun getAsrChecked(): Boolean = rocket.readBoolean(NOTIFY_USER_FOR_ASR)

    private fun getMaghribChecked(): Boolean = rocket.readBoolean(NOTIFY_USER_FOR_MAGHRIB)

    private fun getIshaChecked(): Boolean = rocket.readBoolean(NOTIFY_USER_FOR_ISHA)

    private fun readCountryAndCapital(): String? =
        "${rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY)} , ${rocket.readString(
            COUNTRY_SHARED_PREFERENCE_KEY
        )}"
}