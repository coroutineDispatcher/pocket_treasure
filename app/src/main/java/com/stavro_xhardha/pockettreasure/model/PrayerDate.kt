package com.stavro_xhardha.pockettreasure.model

import com.squareup.moshi.Json

data class PrayerDate(
    @field:Json(name = "readable")
    val readableDate: String,

    @field:Json(name = "timestamp")
    val timestamp: String,

    @field:Json(name = "hijri")
    val hijriPrayerDate: HijriDate,

    @field:Json(name = "gregorian")
    val gregorianDate: GregorianDate
)