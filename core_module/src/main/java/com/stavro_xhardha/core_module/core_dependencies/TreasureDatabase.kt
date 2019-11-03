package com.stavro_xhardha.core_module.core_dependencies

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stavro_xhardha.core_module.model.Aya
import com.stavro_xhardha.core_module.model.Surah

@Database(
    entities = [Surah::class, Aya::class],
    version = 4,
    exportSchema = false
)
abstract class TreasureDatabase : RoomDatabase() {
    abstract fun surahsDao(): SurahsDao

    abstract fun ayasDao(): AyasDao
}