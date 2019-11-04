package com.sxhardha.names_module

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sxhardha.names_module.db.NamesDao
import com.sxhardha.names_module.db.NamesDatabase
import com.sxhardha.names_module.model.Name
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NamesReadWrite {
    private lateinit var namesDao: NamesDao
    private lateinit var namesDatabase: NamesDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        namesDatabase =
            Room.inMemoryDatabaseBuilder(context, NamesDatabase::class.java).build()
        namesDao = namesDatabase.namesDao()
    }

    @After
    fun tearDown() {
        namesDatabase.close()
    }

    @Test
    fun nameReadWrite() = runBlocking {
        val nameToInsert =
            Name("rahman", "rahman", 1, null, "no nameMeaning")

        namesDao.insertName(nameToInsert)

        val names = namesDao.selectAllNames()

        assertEquals(listOf(nameToInsert), names)
    }

}