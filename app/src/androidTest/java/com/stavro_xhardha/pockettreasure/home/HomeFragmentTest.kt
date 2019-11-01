package com.stavro_xhardha.pockettreasure.home

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.stavro_xhardha.pockettreasure.R
import HomeFragment
import com.sxhardha.smoothie.Smoothie
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {
    private lateinit var mockNavController: NavController
    private lateinit var homeFragmentScenario: FragmentScenario<HomeFragment>

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(Smoothie.countingIdlingResource)
        mockNavController = mock(NavController::class.java)
        homeFragmentScenario = launchFragmentInContainer()
        homeFragmentScenario.onFragment {
            Navigation.setViewNavController(it.requireView(), mockNavController)
        }
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(Smoothie.countingIdlingResource)
    }

    @Test
    fun whenInternetConnected_ProgressBarShouldBeVisible() {
        onView(withId(R.id.pbHome)).check(matches(not(isDisplayed())))
    }
}