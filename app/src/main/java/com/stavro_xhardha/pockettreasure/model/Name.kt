package com.stavro_xhardha.pockettreasure.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Entity(tableName = "names_of_creator")
@Parcelize
data class Name @JvmOverloads constructor(
    @field:Json(name ="name")
    @ColumnInfo(name = "arabic_name")
    val arabicName: String,

    @field:Json(name ="transliteration")
    @ColumnInfo(name = "transliteration")
    val transliteration: String,

    @field:Json(name ="number")
    @PrimaryKey
    @ColumnInfo(name = "id")
    val number: Int,

    @field:Json(name ="en")
    @Ignore
    val englishNameMeaning: @RawValue EnglishNameMeaning? = null,

    @ColumnInfo(name = "name_meaning")
    val meaning: String
) : Parcelable