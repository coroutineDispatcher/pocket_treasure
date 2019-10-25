package com.stavro_xhardha.pockettreasure.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.stavro_xhardha.pockettreasure.model.Name
import com.stavro_xhardha.pockettreasure.room_db.NamesDao
import com.stavro_xhardha.pockettreasure.room_db.TreasureDatabase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NamesReadWrite {
    private lateinit var namesDao: NamesDao
    private lateinit var treasureDatabase: TreasureDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        treasureDatabase = Room.inMemoryDatabaseBuilder(context, TreasureDatabase::class.java).build()
        namesDao = treasureDatabase.namesDao()
    }

    @After
    fun tearDown() {
        treasureDatabase.close()
    }

    @Test
    fun nameReadWrite() = runBlocking {
        val nameToInsert = Name("rahman", "rahman", 1, null, "no nameMeaning")

        namesDao.insertName(nameToInsert)

        val names = namesDao.selectAllNames()

        assertEquals(listOf(nameToInsert), names)
    }

}