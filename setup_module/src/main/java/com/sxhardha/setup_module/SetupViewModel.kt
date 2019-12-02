package com.sxhardha.setup_module

import android.location.Geocoder
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stavro_xhardha.core_module.Event
import com.stavro_xhardha.core_module.core_dependencies.AppCoroutineDispatchers
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class SetupViewModel @Inject constructor(
    private val appCoroutineDispatchers: AppCoroutineDispatchers,
    private val setupRepository: SetupRepository
) : ViewModel() {

    val pbVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorVisibility: MutableLiveData<Int> = MutableLiveData()
    val contentVisibility: MutableLiveData<Int> = MutableLiveData()
    private val _locationRequestTurnOff = MutableLiveData<Event<String>>()
    private val _serviceNotAvailableVisibility = MutableLiveData<Event<Int>>()
    private val _locationErrorVisibility = MutableLiveData<Event<Int>>()
    private val _prayerNotificationDialogVisibility = MutableLiveData<Event<String>>()
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    val locationRequestTurnOff: LiveData<Event<String>> = _locationRequestTurnOff
    val serviceNotAvailableVisibility: LiveData<Event<Int>> = _serviceNotAvailableVisibility
    val locationErrorToast: LiveData<Event<Int>> = _locationErrorVisibility
    val prayerNotificationDialogViaibility: LiveData<Event<String>> =
        _prayerNotificationDialogVisibility

    fun switchProgressBarOn() {
        pbVisibility.value = View.VISIBLE
        contentVisibility.value = View.GONE
        errorVisibility.value = View.GONE
    }

    fun showContent() {
        pbVisibility.value = View.GONE
        contentVisibility.value = View.VISIBLE
        errorVisibility.value = View.GONE
    }

    fun showErrorLayout() {
        pbVisibility.value = View.GONE
        contentVisibility.value = View.GONE
        errorVisibility.value = View.VISIBLE
    }

    fun updateNotificationFlags() {
        viewModelScope.launch(appCoroutineDispatchers.ioDispatcher + coroutineExceptionHandler) {
            setupRepository.switchNotificationFlags()
        }
    }

    fun convertToAdress(geocoder: Geocoder, latitude: Double, longitude: Double) {
        viewModelScope.launch(appCoroutineDispatchers.ioDispatcher) {
            try {
                val adresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (adresses.size != 0) {
                    val cityName = adresses[0].adminArea
                    val country = adresses[0].countryName
                    setupRepository.updateCountryAndLocation(country, cityName, latitude, longitude)
                    _prayerNotificationDialogVisibility.postValue(Event("PrayerNotificationDialogEvent"))
                }
            } catch (exception: IOException) {
                exception.printStackTrace()
                _serviceNotAvailableVisibility.postValue(Event(R.string.service_not_available))
                setupRepository.writeDefaultValues()
            } catch (illegalArgumentException: IllegalArgumentException) {
                illegalArgumentException.printStackTrace()
                _locationErrorVisibility.postValue(Event(R.string.invalid_coorinates))
                setupRepository.writeDefaultValues()
            } finally {
                _locationRequestTurnOff.postValue(Event("TurnOffLocationEvent"))
            }
        }
    }
}