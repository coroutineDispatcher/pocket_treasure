package com.stavro_xhardha.pockettreasure.model

import kotlinx.serialization.Serializable

@Serializable
data class HijriDate(
    val date: String,
    val day: String,
    val month: HijriMonth,
    val year: String
)