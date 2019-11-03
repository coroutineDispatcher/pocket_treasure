package com.sxhardha.names_module.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NameResponse(
    @Json(name ="code")
    val code: Int,
    @Json(name ="status")
    val status: String,
    @Json(name ="data")
    val data: List<Name>
)