package com.sxhardha.gallery_module.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UnsplashUser(
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val userFullName: String
)
