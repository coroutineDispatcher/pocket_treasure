package com.stavro_xhardha.core_module.brain

import com.google.android.gms.location.LocationResult

interface LocationTrackerListener {
    fun onLocationError()
    fun onLocationResult(locationResult: LocationResult)
}