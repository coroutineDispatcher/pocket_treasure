package com.stavro_xhardha.core_module

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SharedViewModel : ViewModel() {
    private val _onGpsOpened = MutableLiveData<Boolean>()
    private val _onLocationPermissionGranted = MutableLiveData<Boolean>()
    private val _onToolbarTitleRemoveRequested = MutableLiveData<Boolean>()

    val onGpsOpened: LiveData<Boolean> = _onGpsOpened
    val onLocationPermissiongranted: LiveData<Boolean> = _onLocationPermissionGranted
    val onToolbarTitleRemoveRequested = _onToolbarTitleRemoveRequested

    fun onGpsOpened() {
        _onGpsOpened.value = true
    }

    fun onLocationPermissionGranted() {
        _onLocationPermissionGranted.value = true
    }

    fun removeToolbarTitle() {
        _onToolbarTitleRemoveRequested.value = true
    }
}