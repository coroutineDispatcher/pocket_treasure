package com.stavro_xhardha.pockettreasure.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "names_of_creator")
data class Name(
    @SerializedName("name")
    @ColumnInfo(name = "arabic_name")
    var arabicName: String,

    @SerializedName("transliteration")
    @ColumnInfo(name = "transliteration")
    var transliteration: String,

    @SerializedName("number")
    @PrimaryKey
    @ColumnInfo(name = "id")
    var number: Int,

    @SerializedName("en")
    @Ignore
    val englishNameMeaning: EnglishNameMeaning?,

    @ColumnInfo(name = "name_meaning")
    var meaning: String
) {
    constructor() : this("", "", 0, null, "")
}