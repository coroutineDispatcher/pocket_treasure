package com.stavro_xhardha.pockettreasure.model

import kotlinx.serialization.Serializable

@Serializable
data class NameResponse(
    val code: Int,
    val status: String,
    val data: ArrayList<Name>
)