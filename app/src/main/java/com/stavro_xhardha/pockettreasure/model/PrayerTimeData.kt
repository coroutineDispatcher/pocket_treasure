package com.stavro_xhardha.pockettreasure.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PrayerTimeData(
    @Json(name ="timings")
    val timings: PrayerTiming,

    @Json(name ="date")
    val date: PrayerDate
)