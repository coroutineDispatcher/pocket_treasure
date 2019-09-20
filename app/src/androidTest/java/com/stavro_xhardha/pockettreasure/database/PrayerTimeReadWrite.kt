package com.stavro_xhardha.pockettreasure.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.stavro_xhardha.pockettreasure.model.PrayerTiming
import com.stavro_xhardha.pockettreasure.room_db.TreasureDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PrayerTimeReadWrite {

    private lateinit var treasureDatabase: TreasureDatabase
    private lateinit var dao: PrayerTimesDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        treasureDatabase = Room.inMemoryDatabaseBuilder(context, TreasureDatabase::class.java).build()
        dao = treasureDatabase.prayerTimesDao()
    }

    @After
    fun tearDown() {
        treasureDatabase.close()
    }

    @Test
    fun prayersTimeDaoReadWriteTest() = runBlocking {
        val prayerTime = PrayerTiming(
            0, "3:4", "3:4", "3:4", "3:4", "3:4", "3:4", "3:4", "3:4", "3:4",
            1234213124, 0
        )

        dao.insertPrayerTimes(prayerTime)

        val selection = dao.selectAllPrayersWhereDateAndIsFired(1234213124, 0)

//        assertEquals(selection[0].isFired, prayerTime.isFired)
//        assertEquals(selection[0].timestamp, prayerTime.timestamp)
    }

    @Test
    fun prayersTimeDaoUpdateTest() = runBlocking {
        val prayerTime = PrayerTiming(
            0, "3:4", "3:4", "3:4", "3:4", "3:4", "3:4", "3:4", "3:4", "3:4",
            1234213124, 0
        )

        dao.insertPrayerTimes(prayerTime)

        dao.updatePrayersWhehereDate(1234213124, 1)

        val selectionAfterUpdate = dao.selectAllPrayersWhereDate(1234213124)

        //assertEquals(selectionAfterUpdate[0].isFired, 1)
    }

    @Test
    fun prayerTimeDaoDeleteTest() = runBlocking {
        val prayerTime = PrayerTiming(
            0, "3:4", "3:4", "3:4", "3:4", "3:4", "3:4", "3:4", "3:4", "3:4",
            1234213124, 0
        )

        dao.insertPrayerTimes(prayerTime)

        dao.deleteAllDataInside()

        val selectionAfterUpdate = dao.selectAll()


        assertEquals(selectionAfterUpdate.size, 0)

    }

}