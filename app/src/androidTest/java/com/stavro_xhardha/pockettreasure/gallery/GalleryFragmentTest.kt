package com.stavro_xhardha.pockettreasure.gallery

import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.stavro_xhardha.pockettreasure.R
import com.sxhardha.gallery_module.GalleryFragment
import com.stavro_xhardha.pockettreasure.ui.gallery.GalleryFragmentDirections
import com.sxhardha.smoothie.Smoothie
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class GalleryFragmentTest {
    private lateinit var galleryFragmentScenario: FragmentScenario<com.sxhardha.gallery_module.GalleryFragment>
    private lateinit var mockNavController: NavController
    private lateinit var context: Context

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(Smoothie.countingIdlingResource)
        context = ApplicationProvider.getApplicationContext()
        mockNavController = Mockito.mock(NavController::class.java)
        galleryFragmentScenario = launchFragmentInContainer()
        galleryFragmentScenario.onFragment {
            Navigation.setViewNavController(it.requireView(), mockNavController)
        }
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(Smoothie.countingIdlingResource)
        print("TESTING FINISHED")
    }

    @Test
    fun onNoConnection_GalleryFragment_shouldShowErrorLayout() {

        val wifiManager = context.getSystemService(WIFI_SERVICE) as WifiManager
        wifiManager.isWifiEnabled = false

        onView(withId(R.id.llError)).check(matches(isDisplayed()))

        onView(withId(R.id.btnRetry)).perform(click())

        onView(withId(R.id.llError)).check(matches(isDisplayed()))
    }

    @Test
    fun onSuccessFulConnection_ErrorLayoutShouldBeInvisible_AndRecyclerViewVisible() {

        val wifiManager = context.getSystemService(WIFI_SERVICE) as WifiManager
        wifiManager.isWifiEnabled = true

        onView(withId(R.id.rvGallery)).check(matches(isDisplayed()))

        onView(withId(R.id.llError)).check(matches(not(isDisplayed())))
    }

    @Test
    fun onOneItemClick_NavigationShouldSendMeToFullImageFragment() {
        onView(withId(R.id.rvGallery)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                2, click()
            )
        )

        val action =
            GalleryFragmentDirections.actionGalleryFragmentToFullImageFragment(
                "https://images.unsplash.com/photo-1495846192701-65d0d48e66fe?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjc0ODczfQ"
            )

        verify(mockNavController).navigate(action)
    }
}