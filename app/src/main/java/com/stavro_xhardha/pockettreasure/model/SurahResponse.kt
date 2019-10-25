package com.stavro_xhardha.pockettreasure.model

import kotlinx.serialization.Serializable

@Serializable
data class SurahResponse(
    val surahs: List<Surah>
)