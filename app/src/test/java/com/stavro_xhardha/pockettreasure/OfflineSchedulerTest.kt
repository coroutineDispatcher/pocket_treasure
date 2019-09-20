package com.stavro_xhardha.pockettreasure

import android.app.Application
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.stavro_xhardha.pockettreasure.brain.WORKER_FIRED_KEY
import com.stavro_xhardha.rocket.Rocket
import kotlinx.coroutines.runBlocking
import org.joda.time.DateTime
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`

@RunWith(JUnit4::class)
class OfflineSchedulerTest {
    private lateinit var offlinePrayerScheduler: OfflinePrayerScheduler
    private lateinit var rocket: Rocket
    private lateinit var context: Application
    private lateinit var prayerTimesDao: PrayerTimesDao

    @Before
    fun setUp() {
        context = mock()
        rocket = mock()
        prayerTimesDao = mock()
        offlinePrayerScheduler = OfflinePrayerScheduler(rocket, context, prayerTimesDao)
    }

    @After
    fun tearDown() {

    }

    @Test
    @Throws(RuntimeException::class)
    fun `on same year, query brought nothing`() {
        runBlocking {
            val calendarHelper = DateTime().withTime(9, 1, 1, 0)
            `when`(prayerTimesDao.selectAllPrayersWhereDateAndIsFired(calendarHelper.millis, 0)).thenReturn(null)

            verify(prayerTimesDao, times(0)).deleteAllDataInside()
            verify(rocket, times(0)).writeBoolean(WORKER_FIRED_KEY, false)
        }
    }
}