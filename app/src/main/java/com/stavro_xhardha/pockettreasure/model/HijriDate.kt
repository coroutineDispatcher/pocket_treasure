package com.stavro_xhardha.pockettreasure.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HijriDate(
    @Json(name ="date")
    val date: String,

    @Json(name ="day")
    val day: String,

    @Json(name ="month")
    val hirjiMonth: HijriMonth,

    @Json(name ="year")
    val year: String
)