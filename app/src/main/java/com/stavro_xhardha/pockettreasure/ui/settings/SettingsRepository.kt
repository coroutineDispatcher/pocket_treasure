package com.stavro_xhardha.pockettreasure.ui.settings

import com.stavro_xhardha.core_module.brain.*
import com.stavro_xhardha.rocket.Rocket
import javax.inject.Inject

class SettingsRepository @Inject constructor(val rocket: Rocket) {

    suspend fun getFajrChecked(): Boolean = rocket.readBoolean(NOTIFY_USER_FOR_FAJR)

    suspend fun getDhuhrChecked(): Boolean = rocket.readBoolean(NOTIFY_USER_FOR_DHUHR)

    suspend fun getAsrChecked(): Boolean = rocket.readBoolean(NOTIFY_USER_FOR_ASR)

    suspend fun getMaghribChecked(): Boolean = rocket.readBoolean(NOTIFY_USER_FOR_MAGHRIB)

    suspend fun getIshaChecked(): Boolean = rocket.readBoolean(NOTIFY_USER_FOR_ISHA)

    suspend fun putFajrNotification(oppositeValue: Boolean) {
        rocket.writeBoolean(NOTIFY_USER_FOR_FAJR, oppositeValue)
    }

    suspend fun putAsrNotification(oppositeValue: Boolean) {
        rocket.writeBoolean(NOTIFY_USER_FOR_ASR, oppositeValue)
    }

    suspend fun putMaghribNotification(oppositeValue: Boolean) {
        rocket.writeBoolean(NOTIFY_USER_FOR_MAGHRIB, oppositeValue)
    }

    suspend fun putIshaNotification(oppositeValue: Boolean) {
        rocket.writeBoolean(NOTIFY_USER_FOR_ISHA, oppositeValue)
    }

    suspend fun putDhuhrNotification(oppositeValue: Boolean) {
        rocket.writeBoolean(NOTIFY_USER_FOR_DHUHR, oppositeValue)
    }

    suspend fun readCountryAndCapital(): String? =
        "${rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY)} , ${rocket.readString(
            COUNTRY_SHARED_PREFERENCE_KEY
        )}"

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
}