package com.stavro_xhardha.pockettreasure.model

import com.squareup.moshi.Json

data class UnsplashUrl(
    @field:Json(name = "raw")
    val raw: String,
    @field:Json(name = "full")
    val fullUrl: String,
    @field:Json(name = "regular")
    val regularUrl: String,
    @field:Json(name = "thumb")
    val thumbnailUrl: String
)