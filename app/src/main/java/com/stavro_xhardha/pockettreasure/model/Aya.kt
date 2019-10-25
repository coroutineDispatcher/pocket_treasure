package com.stavro_xhardha.pockettreasure.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(
    tableName = "ayas",
    foreignKeys = [ForeignKey(
        entity = Surah::class,
        parentColumns = arrayOf("surah_number"),
        childColumns = arrayOf("surahs_number"),
        onDelete = CASCADE
    )]
)
data class Aya(
    @ColumnInfo(name = "aya_id")
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @field:Json(name ="audio")
    @ColumnInfo(name = "audio_url")
    val audioUrl: String,

    @field:Json(name ="text")
    @ColumnInfo(name = "ayat_text")
    val ayatText: String,

    @field:Json(name ="numberInSurah")
    @ColumnInfo(name = "ayat_number")
    val ayatNumber: Int,

    @field:Json(name ="juz")
    @ColumnInfo(name = "juz_number")
    val juz: Int,

    @ColumnInfo(name = "surahs_number", index = true)
    val surahNumber: Int
)