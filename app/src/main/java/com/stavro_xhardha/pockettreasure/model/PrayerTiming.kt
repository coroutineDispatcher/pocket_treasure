package com.stavro_xhardha.pockettreasure.model

import com.squareup.moshi.Json

class PrayerTiming(
    @field:Json(name = "Fajr")
    val fajr: String,

    @field:Json(name = "Sunrise")
    val sunrise: String,

    @field:Json(name = "Dhuhr")
    val dhuhr: String,

    @field:Json(name = "Asr")
    val asr: String,

    @field:Json(name = "Sunset")
    val sunset: String,

    @field:Json(name = "Maghrib")
    val magrib: String,

    @field:Json(name = "Isha")
    val isha: String,

    @field:Json(name = "Imsak")
    val imsak: String,

    @field:Json(name = "Midnight")
    val midnight: String
)