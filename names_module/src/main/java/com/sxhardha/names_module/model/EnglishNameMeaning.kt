package com.sxhardha.names_module.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EnglishNameMeaning(@Json(name = "meaning") val meaning: String)