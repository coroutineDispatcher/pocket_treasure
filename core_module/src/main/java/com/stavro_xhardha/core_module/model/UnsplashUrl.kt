package com.stavro_xhardha.core_module.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UnsplashUrl(
    @Json(name = "raw")
    val raw: String,
    @Json(name = "full")
    val fullUrl: String,
    @Json(name = "regular")
    val regularUrl: String,
    @Json(name = "thumb")
    val thumbnailUrl: String
)