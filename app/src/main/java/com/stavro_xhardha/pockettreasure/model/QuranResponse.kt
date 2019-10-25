package com.stavro_xhardha.pockettreasure.model

import kotlinx.serialization.Serializable

@Serializable
data class QuranResponse(
    val code: Int,
    val status: String,
    val data: SurahResponse
)