package com.stavro_xhardha.pockettreasure.model

import kotlinx.serialization.Serializable

@Serializable
data class HijriMonth(
    val number: Int,
    val en: String
)