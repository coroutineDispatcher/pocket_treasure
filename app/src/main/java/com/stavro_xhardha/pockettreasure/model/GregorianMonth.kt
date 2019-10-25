package com.stavro_xhardha.pockettreasure.model

import com.squareup.moshi.Json

data class GregorianMonth(
    @field:Json(name ="number")
    val number: Int,

    @field:Json(name ="en")
    val monthNameInEnglish: String
)