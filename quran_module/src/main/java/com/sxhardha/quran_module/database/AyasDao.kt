package com.sxhardha.quran_module.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sxhardha.quran_module.model.Aya

@Dao
interface AyasDao {
    @Query("SELECT * FROM ayas")
    suspend fun getAllAyas(): List<Aya>

    @Query("SELECT * FROM ayas WHERE surahs_number = :surahNumber")
    fun getAyasBySurahNumber(surahNumber: Int): androidx.paging.DataSource.Factory<Int, Aya>

    @Insert
    suspend fun insertAya(aya: Aya)

    @Query("DELETE FROM ayas")
    suspend fun deleteAllAyas()
}