package com.stavro_xhardha.pockettreasure.model

import com.squareup.moshi.Json

data class SurahResponse(
    @field:Json(name ="surahs")
    val surahs: List<Surah>
)