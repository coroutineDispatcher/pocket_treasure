package com.stavro_xhardha.pockettreasure.model

import com.squareup.moshi.Json

data class UnsplashResult(
    @field:Json(name ="id")
    val id: String,
    @field:Json(name ="description")
    val description: String?,
    @field:Json(name ="alt_description")
    val altDescription: String?,
    @field:Json(name ="urls")
    val photoUrls: UnsplashUrl,
    @field:Json(name ="links")
    val links: UnsplashLink,
    @field:Json(name ="user")
    val user: UnsplashUser
)