package com.stavro_xhardha.core_module.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HomePrayerTime(
    val name: String,
    val time: String,
    var backgroundColor: Int,
    val icon: Int
): Parcelable