package com.stavro_xhardha.pockettreasure.ui.setup

import android.location.Geocoder
import android.view.View
import androidx.lifecycle.*
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class SetupViewModel @AssistedInject constructor(
    private val setupRepository: SetupRepository,
    @Assisted val savedStateHandle: SavedStateHandle
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory {
        fun create(savedStateHandle: SavedStateHandle): SetupViewModel
    }

    val pbVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorVisibility: MutableLiveData<Int> = MutableLiveData()
    val contentVisibility: MutableLiveData<Int> = MutableLiveData()
    private val _locationRequestTurnOff = MutableLiveData<Boolean>()
    private val _serviceNotAvailableVisibility = MutableLiveData<Boolean>()
    private val _locationErrorVisibility = MutableLiveData<Boolean>()
    private val _prayerNotificationDialogVisibility = MutableLiveData<Boolean>()
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    val locationRequestTurnOff: LiveData<Boolean> = _locationRequestTurnOff
    val serviceNotAvailableVisibility: LiveData<Boolean> = _serviceNotAvailableVisibility
    val locationErrorVisibility: LiveData<Boolean> = _locationErrorVisibility
    val prayerNotificationDialogViaibility: LiveData<Boolean> = _prayerNotificationDialogVisibility
    private var locationTurnOfRequested: Boolean = false

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
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            setupRepository.switchNotificationFlags()
        }
    }

    fun convertToAdress(geocoder: Geocoder, latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val adresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (adresses.size != 0) {
                    val cityName = adresses[0].adminArea
                    val country = adresses[0].countryName
                    setupRepository.updateCountryAndLocation(country, cityName, latitude, longitude)
                    if (!locationTurnOfRequested) {
                        _prayerNotificationDialogVisibility.postValue(true)
                        locationTurnOfRequested = true
                    }
                }
            } catch (exception: IOException) {
                exception.printStackTrace()
                _serviceNotAvailableVisibility.postValue(true)
                setupRepository.writeDefaultValues()
            } catch (illegalArgumentException: IllegalArgumentException) {
                illegalArgumentException.printStackTrace()
                _locationErrorVisibility.postValue(true)
                setupRepository.writeDefaultValues()
            } finally {
                _locationRequestTurnOff.postValue(true)
            }
        }
    }
}