package com.stavro_xhardha.pockettreasure.model

import com.squareup.moshi.Json

data class UnsplashUser(
    @field:Json(name ="id")
    val id: String,
    @field:Json(name ="name")
    val userFullName: String
)
