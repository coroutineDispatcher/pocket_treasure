package com.stavro_xhardha.pockettreasure.model

import kotlinx.serialization.Serializable

@Serializable
data class UnsplashUser(
    val id: String,
    val name: String
)
