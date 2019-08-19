package com.stavro_xhardha.pockettreasure.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

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

    @SerializedName("audio")
    @ColumnInfo(name = "audio_url")
    val audioUrl: String,

    @SerializedName("text")
    @ColumnInfo(name = "ayat_text")
    val ayatText: String,

    @SerializedName("numberInSurah")
    @ColumnInfo(name = "ayat_number")
    val ayatNumber: Int,

    @SerializedName("juz")
    @ColumnInfo(name = "juz_number")
    val juz: Int,

    @ColumnInfo(name = "surahs_number", index = true)
    val surahNumber: Int
)