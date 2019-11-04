package com.sxhardha.quran_module.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuranResponse(
    @Json(name ="code")
    val code: Int,
    @Json(name ="status")
    val status: String,
    @Json(name ="data")
    val data: SurahResponse
)