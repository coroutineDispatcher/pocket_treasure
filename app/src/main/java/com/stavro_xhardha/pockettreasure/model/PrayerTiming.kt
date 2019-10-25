package com.stavro_xhardha.pockettreasure.model

import kotlinx.serialization.Serializable

@Serializable
class PrayerTiming(
    val Fajr: String,
    val Sunrise: String,
    val Dhuhr: String,
    val Asr: String,
    val Sunset: String,
    val Maghrib: String,
    val Isha: String,
    val Imsak: String,
    val Midnight: String
)