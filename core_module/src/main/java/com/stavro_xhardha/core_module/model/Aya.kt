package com.stavro_xhardha.core_module.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(
    tableName = "ayas",
    foreignKeys = [ForeignKey(
        entity = Surah::class,
        parentColumns = arrayOf("surah_number"),
        childColumns = arrayOf("surahs_number"),
        onDelete = CASCADE
    )]
)
@JsonClass(generateAdapter = true)
data class Aya(
    @ColumnInfo(name = "aya_id")
    @PrimaryKey(autoGenerate = true)
    @Transient
    val id: Int = 0,

    @Json(name = "audio")
    @ColumnInfo(name = "audio_url")
    val audioUrl: String,

    @Json(name = "text")
    @ColumnInfo(name = "ayat_text")
    val ayatText: String,

    @Json(name = "numberInSurah")
    @ColumnInfo(name = "ayat_number")
    val ayatNumber: Int,

    @Json(name = "juz")
    @ColumnInfo(name = "juz_number")
    val juz: Int,

    @ColumnInfo(name = "surahs_number", index = true)
    @Transient
    val surahNumber: Int = 0
)