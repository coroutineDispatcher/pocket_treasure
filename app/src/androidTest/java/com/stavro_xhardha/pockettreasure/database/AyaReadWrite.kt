package com.stavro_xhardha.pockettreasure.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.stavro_xhardha.pockettreasure.model.Aya
import com.stavro_xhardha.pockettreasure.model.Surah
import com.stavro_xhardha.pockettreasure.room_db.AyasDao
import com.stavro_xhardha.pockettreasure.room_db.SurahsDao
import com.stavro_xhardha.pockettreasure.room_db.TreasureDatabase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AyaReadWrite {

    private lateinit var ayasDao: AyasDao
    private lateinit var treasureDatabase: TreasureDatabase
    private lateinit var surahsDao: SurahsDao
    private val aya = Aya(1, "empty", "no need for that", 2, 1, 5)
    private val surah = Surah(5, "Abc", "Def", "Ghi", "Jkl", listOf())

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        treasureDatabase =
            Room.inMemoryDatabaseBuilder(context, TreasureDatabase::class.java).build()
        ayasDao = treasureDatabase.ayasDao()
        surahsDao = treasureDatabase.surahsDao()
    }

    @After
    fun finish() {
        treasureDatabase.close()
    }

    @Test
    fun writeAyaAndReadIt() = runBlocking {
        //Act
        surahsDao.insertSurah(surah)
        ayasDao.insertAya(aya)
        val insertedAyaList = ayasDao.getAllAyas()
        //Assert

        assertEquals(insertedAyaList, listOf(aya))
    }

    @Test
    @Throws(Exception::class)
    fun deleteAyaTest() = runBlocking {
        //Act
        surahsDao.insertSurah(surah)
        ayasDao.insertAya(aya)
        surahsDao.deleteAllSurahs()
        ayasDao.deleteAllAyas()
        val insertedAyaList = ayasDao.getAllAyas()

        //Assert
        assertEquals(insertedAyaList, listOf<Aya>())
    }
}