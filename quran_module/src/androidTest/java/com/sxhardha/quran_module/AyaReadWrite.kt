package com.sxhardha.quran_module

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sxhardha.quran_module.model.Aya
import com.sxhardha.quran_module.model.Surah
import com.sxhardha.quran_module.database.AyasDao
import com.sxhardha.quran_module.database.QuranDatabase
import com.sxhardha.quran_module.database.SurahsDao
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AyaReadWrite {

    private lateinit var ayasDao: AyasDao
    private lateinit var quranDatabase: QuranDatabase
    private lateinit var surahsDao: SurahsDao
    private val aya =
        Aya(1, "empty", "no need for that", 2, 1, 5)
    private val surah =
        Surah(5, "Abc", "Def", "Ghi", "Jkl", listOf())

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        quranDatabase =
            Room.inMemoryDatabaseBuilder(context, QuranDatabase::class.java).build()
        ayasDao = quranDatabase.ayasDao()
        surahsDao = quranDatabase.surahsDao()
    }

    @After
    fun finish() {
        quranDatabase.close()
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