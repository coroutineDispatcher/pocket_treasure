package com.stavro_xhardha.pockettreasure.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(
    tableName = "ayas",
    foreignKeys = [ForeignKey(
        entity = Surah::class,
        parentColumns = arrayOf("surah_number"),
        childColumns = arrayOf("surahs_number"),
        onDelete = CASCADE
    )]
)
@Serializable
data class Aya(
    @ColumnInfo(name = "aya_id")
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "audio_url")
    val audio: String,

    @ColumnInfo(name = "ayat_text")
    val text: String,

    @ColumnInfo(name = "ayat_number")
    val numberInSurah: Int,

    @ColumnInfo(name = "juz_number")
    val juz: Int,

    @ColumnInfo(name = "surahs_number", index = true)
    val surahNumber: Int
)