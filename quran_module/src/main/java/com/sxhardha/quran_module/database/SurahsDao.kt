package com.sxhardha.quran_module.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sxhardha.quran_module.model.Surah

@Dao
interface SurahsDao {
    @Query("SELECT * FROM surahs")
    suspend fun getAllSuras(): List<Surah>

    @Insert
    suspend fun insertSurah(surah: Surah)

    @Query("Delete FROM surahs")
    suspend fun deleteAllSurahs()
}