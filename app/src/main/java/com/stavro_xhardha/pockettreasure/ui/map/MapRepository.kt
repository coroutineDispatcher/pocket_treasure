//package com.stavro_xhardha.pockettreasure.ui.map
//
//import com.stavro_xhardha.pockettreasure.brain.CAPITAL_SHARED_PREFERENCES_KEY
//import com.stavro_xhardha.rocket.Rocket
//import javax.inject.Inject
//
//class MapRepository @Inject constructor(
//    private val rocket: Rocket
//) {
//    suspend fun readCurrentSavedCity(): String? = rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY)
//}