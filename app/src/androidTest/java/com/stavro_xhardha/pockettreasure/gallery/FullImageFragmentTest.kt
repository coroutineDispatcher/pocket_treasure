package com.stavro_xhardha.pockettreasure.gallery

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sxhardha.gallery_module.image.FullImageFragment
import com.sxhardha.smoothie.Smoothie
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class FullImageFragmentTest {
    private lateinit var fullImageFragmentScenario: FragmentScenario<com.sxhardha.gallery_module.image.FullImageFragment>
    private lateinit var mockNavController: NavController

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(Smoothie.countingIdlingResource)
        mockNavController = Mockito.mock(NavController::class.java)
        fullImageFragmentScenario = launchFragmentInContainer()
        fullImageFragmentScenario.onFragment {
            Navigation.setViewNavController(it.requireView(), mockNavController)
        }
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(Smoothie.countingIdlingResource)
        print("TESTING FINISHED")
    }

    @Test
    fun whenSucessfulImageLoaded_menuItemsShouldBeDisplayed() {

    }
}