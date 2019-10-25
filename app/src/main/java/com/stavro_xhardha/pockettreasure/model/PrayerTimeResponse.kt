package com.stavro_xhardha.pockettreasure.model

import kotlinx.serialization.Serializable

@Serializable
data class PrayerTimeResponse(
    val code: Int,
    val status: String,
    val data: PrayerTimeData
)