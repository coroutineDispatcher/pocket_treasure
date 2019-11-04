package com.sxhardha.quran_module.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sxhardha.quran_module.model.Aya
import com.sxhardha.quran_module.model.Surah

@Database(
    version = 1,
    entities = [Surah::class, Aya::class]
)
abstract class QuranDatabase : RoomDatabase() {
    abstract fun surahsDao(): SurahsDao
    abstract fun ayasDao(): AyasDao
}