package com.stavro_xhardha.pockettreasure.model

import com.squareup.moshi.Json

data class GregorianDate(
    @field:Json(name ="date")
    val date: String,

    @field:Json(name ="day")
    val day: String,

    @field:Json(name ="month")
    val gregorianMonth: GregorianMonth,

    @field:Json(name ="year")
    val year: String
)