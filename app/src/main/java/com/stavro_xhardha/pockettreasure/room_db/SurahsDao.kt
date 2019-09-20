package com.stavro_xhardha.pockettreasure.room_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.stavro_xhardha.pockettreasure.model.Surah

@Dao
interface SurahsDao {
    @Query("SELECT * FROM surahs")
    suspend fun getAllSuras(): List<Surah>

    @Insert
    suspend fun insertSurah(surah: Surah)

    @Query("Delete FROM surahs")
    suspend fun deleteAllSurahs()
}