package com.stavro_xhardha.pockettreasure.model

import com.google.gson.annotations.SerializedName

class PrayerTiming(
    @SerializedName("Fajr")
    val fajr: String,

    @SerializedName("Sunrise")
    val sunrise: String,

    @SerializedName("Dhuhr")
    val dhuhr: String,

    @SerializedName("Asr")
    val asr: String,

    @SerializedName("Sunset")
    val sunset: String,

    @SerializedName("Maghrib")
    val magrib: String,

    @SerializedName("Isha")
    val isha: String,

    @SerializedName("Imsak")
    val imsak: String,

    @SerializedName("Midnight")
    val midnight: String
)