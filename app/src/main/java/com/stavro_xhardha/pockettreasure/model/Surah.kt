package com.stavro_xhardha.pockettreasure.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "surahs")
@JsonClass(generateAdapter = true)
data class Surah @JvmOverloads constructor(
    @Json(name ="number")
    @PrimaryKey
    @ColumnInfo(name = "surah_number")
    val surahNumber: Int,

    @Json(name ="name")
    @ColumnInfo(name = "surah_arabic_name")
    val surahArabicName: String,

    @Json(name ="englishName")
    @ColumnInfo(name = "surah_english_name")
    val englishName: String,

    @Json(name ="englishNameTranslation")
    @ColumnInfo(name = "surah_english_translation")
    val englishTranslation: String,

    @Json(name ="revelationType")
    @ColumnInfo(name = "surah_relevation_type")
    val revelationType: String,

    @Json(name ="ayahs")
    @Ignore
    val ayas: List<Aya> = listOf()
)