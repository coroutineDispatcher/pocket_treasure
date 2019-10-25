package com.stavro_xhardha.pockettreasure.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "surahs")
data class Surah @JvmOverloads constructor(
    @field:Json(name ="number")
    @PrimaryKey
    @ColumnInfo(name = "surah_number")
    val surahNumber: Int,

    @field:Json(name ="name")
    @ColumnInfo(name = "surah_arabic_name")
    val surahArabicName: String,

    @field:Json(name ="englishName")
    @ColumnInfo(name = "surah_english_name")
    val englishName: String,

    @field:Json(name ="englishNameTranslation")
    @ColumnInfo(name = "surah_english_translation")
    val englishTranslation: String,

    @field:Json(name ="revelationType")
    @ColumnInfo(name = "surah_relevation_type")
    val revelationType: String,

    @field:Json(name ="ayahs")
    @Ignore
    val ayas: List<Aya> = listOf()
)