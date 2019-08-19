package com.stavro_xhardha.pockettreasure.model

import android.os.Parcel
import android.os.Parcelable
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
    var englishNameMeaning: EnglishNameMeaning?,

    @ColumnInfo(name = "name_meaning")
    var meaning: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        null,
        parcel.readString()!!
    )

    constructor() : this("", "", 0, null, "")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(arabicName)
        parcel.writeString(transliteration)
        parcel.writeInt(number)
        parcel.writeString(meaning)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Name> {
        override fun createFromParcel(parcel: Parcel): Name {
            return Name(parcel)
        }

        override fun newArray(size: Int): Array<Name?> {
            return arrayOfNulls(size)
        }
    }
}