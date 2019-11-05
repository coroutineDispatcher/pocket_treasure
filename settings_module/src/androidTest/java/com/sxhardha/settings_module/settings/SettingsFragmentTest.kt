package com.sxhardha.settings_module.settings

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sxhardha.settings_module.R
import com.sxhardha.smoothie.Smoothie
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class SettingsFragmentTest {

    private lateinit var settingsFragmentScenario: FragmentScenario<com.sxhardha.settings_module.SettingsFragment>
    private lateinit var mockNavController: NavController

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(Smoothie.countingIdlingResource)
        mockNavController = mock(NavController::class.java)
        settingsFragmentScenario = launchFragmentInContainer()
        settingsFragmentScenario.onFragment {
            Navigation.setViewNavController(it.requireView(), mockNavController)
        }
    }

    @After
    fun finish() {
        IdlingRegistry.getInstance().unregister(Smoothie.countingIdlingResource)
    }

    @Test
    fun testFajrClick() {
        performClickOnViews(R.id.swFajr)

        settingsFragmentScenario.recreate()

        onViewWithId(R.id.swFajr).check(ViewAssertions.matches(isChecked()))

        performClickOnViews(R.id.swFajr)

        settingsFragmentScenario.recreate()

        onViewWithId(R.id.swFajr).check(ViewAssertions.matches(isNotChecked()))
    }

    @Test
    fun testDhuhrClick() {
        performClickOnViews(R.id.swDhuhr)

        settingsFragmentScenario.recreate()

        onViewWithId(R.id.swDhuhr).check(ViewAssertions.matches(isChecked()))

        performClickOnViews(R.id.swDhuhr)

        settingsFragmentScenario.recreate()

        onViewWithId(R.id.swDhuhr).check(ViewAssertions.matches(isNotChecked()))
    }

    @Test
    fun testAsrClick() {
        performClickOnViews(R.id.swAsr)

        settingsFragmentScenario.recreate()

        onViewWithId(R.id.swAsr).check(ViewAssertions.matches(isChecked()))

        performClickOnViews(R.id.swAsr)

        settingsFragmentScenario.recreate()

        onViewWithId(R.id.swAsr).check(ViewAssertions.matches(isNotChecked()))
    }

    @Test
    fun testMaghribClick() {
        performClickOnViews(R.id.swMaghrib)

        settingsFragmentScenario.recreate()

        onViewWithId(R.id.swMaghrib).check(ViewAssertions.matches(isChecked()))

        performClickOnViews(R.id.swMaghrib)

        settingsFragmentScenario.recreate()

        onViewWithId(R.id.swMaghrib).check(ViewAssertions.matches(isNotChecked()))
    }

    @Test
    fun testIshaClick() {
        performClickOnViews(R.id.swIsha)

        settingsFragmentScenario.recreate()

        onViewWithId(R.id.swIsha).check(ViewAssertions.matches(isChecked()))

        performClickOnViews(R.id.swIsha)

        settingsFragmentScenario.recreate()

        onViewWithId(R.id.swIsha).check(ViewAssertions.matches(isNotChecked()))
    }

    @Test
    fun testBackButtonClick() {
        pressBack()
        verify(mockNavController).popBackStack(R.id.homeFragment, false)
    }

    private fun performClickOnViews(id: Int) {
        onViewWithId(id).perform(click())
    }

    private fun onViewWithId(id: Int) = onView(withId(id))
}