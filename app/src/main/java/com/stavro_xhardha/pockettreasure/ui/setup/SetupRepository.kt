package com.stavro_xhardha.pockettreasure.ui.setup

import com.stavro_xhardha.core_module.brain.*
import com.stavro_xhardha.rocket.Rocket
import javax.inject.Inject

class SetupRepository @Inject constructor(
    private val rocket: Rocket
) {

    suspend fun isCountryOrCapitalEmpty(): Boolean {
        return rocket.readString(COUNTRY_SHARED_PREFERENCE_KEY)!!.isEmpty()
                || rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY)!!.isEmpty()
    }

    suspend fun switchNotificationFlags() {
        rocket.writeBoolean(NOTIFY_USER_FOR_FAJR, true)
        rocket.writeBoolean(NOTIFY_USER_FOR_DHUHR, true)
        rocket.writeBoolean(NOTIFY_USER_FOR_ASR, true)
        rocket.writeBoolean(NOTIFY_USER_FOR_MAGHRIB, true)
        rocket.writeBoolean(NOTIFY_USER_FOR_ISHA, true)
    }

    suspend fun updateCountryAndLocation(
        country: String,
        cityName: String,
        latitude: Double,
        longitude: Double
    ) {
        rocket.writeString(COUNTRY_SHARED_PREFERENCE_KEY, country)
        rocket.writeString(CAPITAL_SHARED_PREFERENCES_KEY, cityName)
        rocket.writeBoolean(COUNTRY_UPDATED, true)
        rocket.writeFloat(LATITUDE_KEY, latitude.toFloat())
        rocket.writeFloat(LONGITUDE_KEY, longitude.toFloat())
    }

    suspend fun writeDefaultValues() {
        rocket.writeFloat(LATITUDE_KEY, 0.toFloat())
        rocket.writeFloat(LONGITUDE_KEY, 0.toFloat())
    }

}