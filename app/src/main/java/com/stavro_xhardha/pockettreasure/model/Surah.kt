package com.stavro_xhardha.pockettreasure.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "surahs")
data class Surah @JvmOverloads constructor(
    @SerializedName("number")
    @PrimaryKey
    @ColumnInfo(name = "surah_number")
    val surahNumber: Int,

    @SerializedName("name")
    @ColumnInfo(name = "surah_arabic_name")
    val surahArabicName: String,

    @SerializedName("englishName")
    @ColumnInfo(name = "surah_english_name")
    val englishName: String,

    @SerializedName("englishNameTranslation")
    @ColumnInfo(name = "surah_english_translation")
    val englishTranslation: String,

    @SerializedName("revelationType")
    @ColumnInfo(name = "surah_relevation_type")
    val revelationType: String,

    @SerializedName("ayahs")
    @Ignore
    val ayas: List<Aya> = listOf()
)