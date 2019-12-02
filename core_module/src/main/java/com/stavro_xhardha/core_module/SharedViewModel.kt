package com.stavro_xhardha.core_module

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SharedViewModel : ViewModel() {
    private val _onGpsOpened = MutableLiveData<Event<String>>()
    private val _onLocationPermissionGranted = MutableLiveData<Event<String>>()
    private val _onToolbarTitleRemoveRequested = MutableLiveData<Event<String>>()

    val onGpsOpened: LiveData<Event<String>> = _onGpsOpened
    val onLocationPermissiongranted: LiveData<Event<String>> = _onLocationPermissionGranted
    val onToolbarTitleRemoveRequested: LiveData<Event<String>> = _onToolbarTitleRemoveRequested

    fun onGpsOpened() {
        _onGpsOpened.value = Event("OnGPSOpened")
    }

    fun onLocationPermissionGranted() {
        _onLocationPermissionGranted.value = Event("OnLocationPermissionGranted")
    }

    fun removeToolbarTitle() {
        _onToolbarTitleRemoveRequested.value = Event("OnToolbarTitleRemoveRequested")
    }
}