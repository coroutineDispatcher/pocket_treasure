package com.stavro_xhardha.pockettreasure.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class PrayerTiming(
    @Json(name = "Fajr")
    val fajr: String,

    @Json(name = "Sunrise")
    val sunrise: String,

    @Json(name = "Dhuhr")
    val dhuhr: String,

    @Json(name = "Asr")
    val asr: String,

    @Json(name = "Sunset")
    val sunset: String,

    @Json(name = "Maghrib")
    val magrib: String,

    @Json(name = "Isha")
    val isha: String,

    @Json(name = "Imsak")
    val imsak: String,

    @Json(name = "Midnight")
    val midnight: String
)