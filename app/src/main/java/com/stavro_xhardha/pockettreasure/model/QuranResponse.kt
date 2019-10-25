package com.stavro_xhardha.pockettreasure.model

import com.squareup.moshi.Json

data class QuranResponse(
    @field:Json(name ="code")
    val code: Int,
    @field:Json(name ="status")
    val status: String,
    @field:Json(name ="data")
    val data: SurahResponse
)