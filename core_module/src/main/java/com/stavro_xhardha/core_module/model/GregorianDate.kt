package com.stavro_xhardha.core_module.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GregorianDate(
    @Json(name ="date")
    val date: String,

    @Json(name ="day")
    val day: String,

    @Json(name ="month")
    val gregorianMonth: GregorianMonth,

    @Json(name ="year")
    val year: String
)