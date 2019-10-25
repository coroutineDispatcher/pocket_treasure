package com.stavro_xhardha.pockettreasure.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UnsplashUser(
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val userFullName: String
)
