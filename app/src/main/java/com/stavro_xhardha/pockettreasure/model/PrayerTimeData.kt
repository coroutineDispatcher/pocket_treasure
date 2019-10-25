package com.stavro_xhardha.pockettreasure.model

import kotlinx.serialization.Serializable

@Serializable
data class PrayerTimeData(
    val timings: PrayerTiming,
    val date: PrayerDate
)