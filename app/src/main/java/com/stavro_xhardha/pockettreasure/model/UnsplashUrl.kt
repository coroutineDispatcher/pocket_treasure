package com.stavro_xhardha.pockettreasure.model

import kotlinx.serialization.Serializable

@Serializable
data class UnsplashUrl(
    val raw: String,
    val full: String,
    val regular: String,
    val thumb: String
)