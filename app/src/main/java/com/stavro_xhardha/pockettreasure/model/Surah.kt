package com.stavro_xhardha.pockettreasure.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "surahs")
@Serializable
data class Surah @JvmOverloads constructor(
    @PrimaryKey
    @ColumnInfo(name = "surah_number")
    val number: Int,
    @ColumnInfo(name = "surah_arabic_name")
    val name: String,
    @ColumnInfo(name = "surah_english_name")
    val englishName: String,
    @ColumnInfo(name = "surah_english_translation")
    val englishNameTranslation: String,
    @ColumnInfo(name = "surah_relevation_type")
    val revelationType: String,
    @Ignore
    val ayahs: List<Aya> = listOf()
)