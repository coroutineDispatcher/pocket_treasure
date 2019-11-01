package com.stavro_xhardha.core_module.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EnglishNameMeaning(@Json(name = "meaning") val meaning: String)