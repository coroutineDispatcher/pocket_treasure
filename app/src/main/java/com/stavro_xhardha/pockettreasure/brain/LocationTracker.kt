package com.stavro_xhardha.pockettreasure.brain

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

class LocationTracker(
    val activity: FragmentActivity,
    val locationTrackerListener: LocationTrackerListener
) {

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)

    private var locationCallback: LocationCallback? = null

    private var locationRequest: LocationRequest? = null

    fun startLocationRequestProcess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    activity.applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    activity.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                activity.requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), REQUEST_LOCATION_PERMISSION
                )
            } else {
                getUserLocation()
            }
        } else {
            getUserLocation()
        }
    }

    fun getUserLocation() {
        locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)

        val client: SettingsClient = LocationServices.getSettingsClient(activity)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            if (isDebugMode) Log.d(APPLICATION_TAG, "LOCATION SETTINGS ARE READY, GPS IS ON")
            updateLocation()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(
                        activity,
                        REQUEST_CHECK_LOCATION_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    sendEx.printStackTrace()
                }
            }
        }
    }

    fun updateLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    activity.applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    activity.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                activity.requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), REQUEST_LOCATION_PERMISSION
                )
            } else {
                requestLocationUpdates()
            }
        } else {
            requestLocationUpdates()
        }
    }

    private fun requestLocationUpdates() {
        try {
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    if (locationResult == null) {
                        locationTrackerListener.onLocationError()
                    } else {
                        locationTrackerListener.onLocationResult(locationResult)
                    }
                }
            }
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        } catch (securityException: SecurityException) {
            securityException.printStackTrace()
        }
    }

    fun removeLocationRequest() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}