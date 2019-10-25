package com.stavro_xhardha.pockettreasure.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UnsplashLink(
    @Json(name ="self")
    val selfUrl: String,
    @Json(name ="html")
    val htmlUrl: String,
    @Json(name ="download")
    val downloadUrl: String,
    @Json(name ="download_location")
    val downloadLocationUrl: String
)