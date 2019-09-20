package com.stavro_xhardha.pockettreasure.room_db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.stavro_xhardha.pockettreasure.model.Aya

@Dao
interface AyasDao {
    @Query("SELECT * FROM ayas")
    suspend fun getAllAyas(): List<Aya>

    @Query("SELECT * FROM ayas WHERE surahs_number = :surahNumber")
    fun getAyasBySurahNumber(surahNumber: Int): DataSource.Factory<Int, Aya>

    @Insert
    suspend fun insertAya(aya: Aya)

    @Query("DELETE FROM ayas")
    suspend fun deleteAllAyas()
}