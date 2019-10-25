package com.stavro_xhardha.pockettreasure.model

import kotlinx.serialization.Serializable

@Serializable
data class UnsplashResult(
    val id: String,
    val description: String?,
    val alt_description: String?,
    val urls: UnsplashUrl,
    val links: UnsplashLink,
    val user: UnsplashUser
)