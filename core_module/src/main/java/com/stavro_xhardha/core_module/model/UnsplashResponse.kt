package com.stavro_xhardha.core_module.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UnsplashResponse(
    @Json(name ="total")
    val total: Int,
    @Json(name ="total_pages")
    val totalPages: Int,
    @Json(name ="results")
    val results: List<UnsplashResult>
)