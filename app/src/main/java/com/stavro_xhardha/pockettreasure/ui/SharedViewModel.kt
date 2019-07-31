package com.stavro_xhardha.pockettreasure.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SharedViewModel : ViewModel() {
    private val _onGpsOpened = MutableLiveData<Boolean>()
    private val _onLocationPermissionGranted = MutableLiveData<Boolean>()

    val onGpsOpened: LiveData<Boolean> = _onGpsOpened
    val onLocationPermissiongranted: LiveData<Boolean> = _onLocationPermissionGranted

    fun onGpsOpened() {
        _onGpsOpened.value = true
    }

    fun onLocationPermissionGranted() {
        _onLocationPermissionGranted.value = true
    }
}