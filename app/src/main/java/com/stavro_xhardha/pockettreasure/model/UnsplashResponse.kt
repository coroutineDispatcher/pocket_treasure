package com.stavro_xhardha.pockettreasure.model

import kotlinx.serialization.Serializable

@Serializable
data class UnsplashResponse(
    val total: Int,
    val total_pages: Int,
    val results: List<UnsplashResult>
)