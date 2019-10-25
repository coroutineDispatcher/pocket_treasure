package com.stavro_xhardha.pockettreasure.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GregorianMonth(
    @Json(name ="number")
    val number: Int,

    @Json(name ="en")
    val monthNameInEnglish: String
)