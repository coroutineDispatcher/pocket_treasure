package com.stavro_xhardha.pockettreasure.model

import com.squareup.moshi.Json

data class PrayerTimeData(
    @field:Json(name ="timings")
    val timings: PrayerTiming,

    @field:Json(name ="date")
    val date: PrayerDate
)