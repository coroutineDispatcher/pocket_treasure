package com.stavro_xhardha.pockettreasure.model

import com.squareup.moshi.Json

data class UnsplashResponse(
    @field:Json(name ="total")
    val total: Int,
    @field:Json(name ="total_pages")
    val totalPages: Int,
    @field:Json(name ="results")
    val results: List<UnsplashResult>
)