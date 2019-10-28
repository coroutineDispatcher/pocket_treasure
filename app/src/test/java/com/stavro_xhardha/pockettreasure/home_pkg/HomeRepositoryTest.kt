package com.stavro_xhardha.pockettreasure.home_pkg

import com.nhaarman.mockitokotlin2.mock
import com.stavro_xhardha.core_module.brain.*
import com.stavro_xhardha.core_module.core_dependencies.TreasureApi
import com.stavro_xhardha.pockettreasure.ui.home.HomeRepository
import com.stavro_xhardha.rocket.Rocket
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class HomeRepositoryTest {
    private lateinit var homeRepository: HomeRepository
    private lateinit var treasureApi: TreasureApi
    private lateinit var prayerTimesDao: PrayerTimesDao
    private lateinit var rocket: Rocket

    @Before
    fun setUp() {
        treasureApi = mock()
        rocket = mock()
        prayerTimesDao = mock()
        homeRepository = HomeRepository(treasureApi, rocket, prayerTimesDao)
    }

    @After
    fun tearDown() {
        print("Test has finished")
    }

    @Test
    fun `zero gregorian day should return zero`() {
        runBlocking {
            `when`(rocket.readInt(GREGORIAN_DAY_KEY)).thenReturn(0)

            val day = homeRepository.getCurrentRegisteredDay()

            assertEquals(0, day)
        }
    }

    @Test
    fun `number gregorian day should return the number`() {
        runBlocking {
            `when`(rocket.readInt(GREGORIAN_DAY_KEY)).thenReturn(55)

            val day = homeRepository.getCurrentRegisteredDay()

            assertEquals(55, day)
        }
    }

    @Test
    fun `on empty string fajr should not be saved`() {
        runBlocking {
            homeRepository.saveFajrTime("")

            verify(rocket, times(0)).writeString(FAJR_KEY, "")
        }
    }

    @Test
    fun `on emty string dhuhr should not be saved`() {
        runBlocking {
            homeRepository.saveDhuhrTime("")

            verify(rocket, times(0)).writeString(DHUHR_KEY, "")
        }
    }

    @Test
    fun `on emty string asr should not be saved`() {
        runBlocking {
            homeRepository.saveAsrTime("")

            verify(rocket, times(0)).writeString(ASR_KEY, "")
        }
    }

    @Test
    fun `on emty string maghrib should not be saved`() {
        runBlocking {
            homeRepository.saveMagribTime("")

            verify(rocket, times(0)).writeString(MAGHRIB_KEY, "")
        }
    }

    @Test
    fun `on emty string isha should not be saved`() {
        runBlocking {
            homeRepository.saveIshaTime("")

            verify(rocket, times(0)).writeString(ISHA_KEY, "")
        }
    }

    @Test
    fun `on non qualified value year saveMonthOfYear should not be saved`() {
        runBlocking {
            homeRepository.saveYear(0)

            verify(rocket, times(0)).writeInt(GREGORIAN_YEAR_KEY, 0)
        }
    }

    @Test
    fun `on negative year, saveMonthOfYear should not execute`() {
        runBlocking {
            homeRepository.saveYear(-150)

            verify(rocket, times(0)).writeInt(GREGORIAN_YEAR_KEY, -150)
        }
    }

    @Test
    fun `on positive year saveMonthOfYear should execute`() {
        runBlocking {
            homeRepository.saveYear(1996)

            verify(rocket, times(1)).writeInt(GREGORIAN_YEAR_KEY, 1996)
        }
    }

    @Test
    fun `on empty month name, saving month name method should not execute`() {
        runBlocking {
            homeRepository.saveMonthName("")

            verify(rocket, times(0)).writeString(GREGORIAN_MONTH_NAME_KEY, "")
        }
    }

    @Test
    fun `on given non empty month name, saving month name should execute`() {
        runBlocking {
            homeRepository.saveMonthName("Stavro")

            verify(rocket, times(1)).writeString(GREGORIAN_MONTH_NAME_KEY, "Stavro")
        }
    }

    @Test
    fun `on given empty hijri month, saving hijri month name won't execute`() {
        runBlocking {
            homeRepository.saveMonthOfYearHijri("")

            verify(rocket, times(0)).writeString(HIJRI_MONTH_NAME_KEY, "")
        }
    }

    @Test
    fun `on given non empty hijri month, saving hijri month name won't execute`() {
        runBlocking {
            homeRepository.saveMonthOfYearHijri("Testing")

            verify(rocket, times(1)).writeString(HIJRI_MONTH_NAME_KEY, "Testing")
        }
    }
}