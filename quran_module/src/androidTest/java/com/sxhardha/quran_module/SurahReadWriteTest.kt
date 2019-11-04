package com.sxhardha.quran_module

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sxhardha.quran_module.database.QuranDatabase
import com.sxhardha.quran_module.database.SurahsDao
import com.sxhardha.quran_module.model.Surah
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SurahReadWriteTest {

    private lateinit var surahsDao: SurahsDao
    private lateinit var treasureDatabase: QuranDatabase
    private val surah =
        Surah(1, "Abc", "Def", "Ghi", "Jkl", listOf())

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        treasureDatabase =
            Room.inMemoryDatabaseBuilder(context, QuranDatabase::class.java).build()
        surahsDao = treasureDatabase.surahsDao()
    }

    @After
    fun finish() {
        treasureDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeSurahAndReadIt() = runBlocking {
        //Act
        surahsDao.insertSurah(surah)
        val insertedSurah = surahsDao.getAllSuras()

        //Assert
        assertEquals(listOf(surah), insertedSurah)
    }

    @Test
    @Throws(Exception::class)
    fun deleteSurahTest() = runBlocking {
        //Act
        surahsDao.insertSurah(surah)
        surahsDao.deleteAllSurahs()
        val insertedSurah = surahsDao.getAllSuras()

        //Assert
        assertEquals(insertedSurah, listOf<Surah>())
    }
}