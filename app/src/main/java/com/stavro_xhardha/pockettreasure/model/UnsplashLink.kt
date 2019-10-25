package com.stavro_xhardha.pockettreasure.model

import com.squareup.moshi.Json

data class UnsplashLink(
    @field:Json(name ="self")
    val selfUrl: String,
    @field:Json(name ="html")
    val htmlUrl: String,
    @field:Json(name ="download")
    val downloadUrl: String,
    @field:Json(name ="download_location")
    val downloadLocationUrl: String
)