package com.stavro_xhardha.pockettreasure.model

import com.squareup.moshi.Json

data class HijriDate(
    @field:Json(name ="date")
    val date: String,

    @field:Json(name ="day")
    val day: String,

    @field:Json(name ="month")
    val hirjiMonth: HijriMonth,

    @field:Json(name ="year")
    val year: String
)