package com.stavro_xhardha.core_module.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PrayerDate(
    @Json(name = "readable")
    val readableDate: String,

    @Json(name = "timestamp")
    val timestamp: String,

    @Json(name = "hijri")
    val hijriPrayerDate: HijriDate,

    @Json(name = "gregorian")
    val gregorianDate: GregorianDate
)