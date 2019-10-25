package com.stavro_xhardha.pockettreasure.model

import kotlinx.serialization.Serializable

@Serializable
data class PrayerDate(
    val readable: String,
    val timestamp: String,
    val hijri: HijriDate,
    val gregorian: GregorianDate
)