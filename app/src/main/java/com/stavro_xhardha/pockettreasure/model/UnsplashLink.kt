package com.stavro_xhardha.pockettreasure.model

import kotlinx.serialization.Serializable

@Serializable
data class UnsplashLink(
    val self: String,
    val html: String,
    val download: String,
    val download_location: String
)