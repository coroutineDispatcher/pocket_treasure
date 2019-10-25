package com.stavro_xhardha.pockettreasure.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import kotlinx.serialization.Serializable

@Entity(tableName = "names_of_creator")
@Parcelize
@Serializable
data class Name @JvmOverloads constructor(
    @ColumnInfo(name = "arabic_name")
    val name: String,

    @ColumnInfo(name = "transliteration")
    val transliteration: String,

    @PrimaryKey
    @ColumnInfo(name = "id")
    val number: Int,

    @Ignore
    val en: @RawValue EnglishNameMeaning? = null,

    @ColumnInfo(name = "name_meaning")
    val meaning: String
) : Parcelable