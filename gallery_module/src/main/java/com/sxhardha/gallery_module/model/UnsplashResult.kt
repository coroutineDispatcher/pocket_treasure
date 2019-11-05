package com.sxhardha.gallery_module.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UnsplashResult(
    @Json(name ="id")
    val id: String,
    @Json(name ="description")
    val description: String?,
    @Json(name ="alt_description")
    val altDescription: String?,
    @Json(name ="urls")
    val photoUrls: UnsplashUrl,
    @Json(name ="links")
    val links: UnsplashLink,
    @Json(name ="user")
    val user: UnsplashUser
)