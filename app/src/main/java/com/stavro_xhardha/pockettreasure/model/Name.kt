package com.stavro_xhardha.pockettreasure.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Entity(tableName = "names_of_creator")
@Parcelize
data class Name @JvmOverloads constructor(
    @SerializedName("name")
    @ColumnInfo(name = "arabic_name")
    val arabicName: String,

    @SerializedName("transliteration")
    @ColumnInfo(name = "transliteration")
    val transliteration: String,

    @SerializedName("number")
    @PrimaryKey
    @ColumnInfo(name = "id")
    val number: Int,

    @SerializedName("en")
    @Ignore
    val englishNameMeaning: @RawValue EnglishNameMeaning? = null,

    @ColumnInfo(name = "name_meaning")
    val meaning: String
) : Parcelable