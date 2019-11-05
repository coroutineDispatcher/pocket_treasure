package com.sxhardha.setup_module.setup_pkg

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.stavro_xhardha.core_module.brain.*
import com.stavro_xhardha.core_module.core_dependencies.TreasureApi
import com.stavro_xhardha.rocket.Rocket
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`

@RunWith(JUnit4::class)
class SetupRepositoryTest {
    private lateinit var setupRepository: com.sxhardha.setup_module.SetupRepository
    private lateinit var rocket: Rocket
    private lateinit var treasureApi: TreasureApi

    @Before
    fun setUp() {
        rocket = mock()
        treasureApi = mock()
        setupRepository = com.sxhardha.setup_module.SetupRepository(rocket)
    }

    @After
    fun finish() {
        println("Testing finished")
    }

    @Test
    fun `SetupRepository on given empty country and capital city, invoked method should return true`() {
        runBlocking {
            countryAndCapitalEmpty()

            val repositoryEmpty = setupRepository.isCountryOrCapitalEmpty()

            assertEquals(true, repositoryEmpty)
        }
    }

    @Test
    fun `on country empty and capital not empty method should return true`() {
        runBlocking {
            countryEmptyCapitalNotEmpty()

            val repositoryEmpty = setupRepository.isCountryOrCapitalEmpty()

            assertEquals(true, repositoryEmpty)
        }
    }

    @Test
    fun `on country not empty and capital empty method should return true`() {
        runBlocking {
            countryNotEmptyAndCapitalEmpty()

            val repositoryEmpty = setupRepository.isCountryOrCapitalEmpty()

            assertEquals(true, repositoryEmpty)
        }
    }

    @Test
    fun `on both country and capital not empty method should return false`() {
        runBlocking {
            countryNotEmptyAndCapitalNotEmpty()

            val repositoryEmpty = setupRepository.isCountryOrCapitalEmpty()

            assertEquals(false, repositoryEmpty)
        }
    }

    @Test
    fun `on switch notification flags should execute correctly`() {
        runBlocking {
            setupRepository.switchNotificationFlags()

            verify(rocket, times(1)).writeBoolean(NOTIFY_USER_FOR_FAJR, true)
            verify(rocket, times(1)).writeBoolean(NOTIFY_USER_FOR_DHUHR, true)
            verify(rocket, times(1)).writeBoolean(NOTIFY_USER_FOR_ASR, true)
            verify(rocket, times(1)).writeBoolean(NOTIFY_USER_FOR_MAGHRIB, true)
            verify(rocket, times(1)).writeBoolean(NOTIFY_USER_FOR_ISHA, true)
        }
    }

    private fun countryNotEmptyAndCapitalEmpty() {
        runBlocking {
            `when`(rocket.readString(COUNTRY_SHARED_PREFERENCE_KEY)).thenReturn("Albania")
            `when`(rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY)).thenReturn("")
        }
    }

    private fun countryAndCapitalEmpty() {
        runBlocking {
            `when`(rocket.readString(COUNTRY_SHARED_PREFERENCE_KEY)).thenReturn("")
            `when`(rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY)).thenReturn("")
        }
    }

    private fun countryEmptyCapitalNotEmpty() {
        runBlocking {
            `when`(rocket.readString(COUNTRY_SHARED_PREFERENCE_KEY)).thenReturn("")
            `when`(rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY)).thenReturn("Tirana")
        }
    }

    private fun countryNotEmptyAndCapitalNotEmpty() {
        runBlocking {
            `when`(rocket.readString(COUNTRY_SHARED_PREFERENCE_KEY)).thenReturn("Albania")
            `when`(rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY)).thenReturn("Tirana")
        }
    }
}