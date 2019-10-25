package com.stavro_xhardha.pockettreasure.model

import kotlinx.serialization.Serializable

@Serializable
data class GregorianDate(
    val date: String,
    val day: String,
    val month: GregorianMonth,
    val year: String
)