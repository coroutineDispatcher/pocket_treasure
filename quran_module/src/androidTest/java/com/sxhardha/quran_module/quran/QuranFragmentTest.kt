package com.sxhardha.quran_module.quran

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sxhardha.quran_module.R
import com.sxhardha.quran_module.ui.quran.QuranFragment
import com.sxhardha.smoothie.Smoothie
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito


@RunWith(AndroidJUnit4::class)
class QuranFragmentTest {
    private lateinit var mockNavController: NavController
    private lateinit var quranFragmentScenario: FragmentScenario<QuranFragment>

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(Smoothie.countingIdlingResource)
        mockNavController = Mockito.mock(NavController::class.java)
        quranFragmentScenario = launchFragmentInContainer()
        quranFragmentScenario.onFragment {
            Navigation.setViewNavController(it.requireView(), mockNavController)
        }
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(Smoothie.countingIdlingResource)
    }

    @Test
    fun checkIfProgressBarStarts() {
        onView(withId(R.id.pbQuran)).check(ViewAssertions.matches(isDisplayed()))
    }
}