package com.sxhardha.setup_module.setup_pkg

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.stavro_xhardha.core_module.brain.observeOnce
import com.stavro_xhardha.core_module.core_dependencies.AppCoroutineDispatchers
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class SetupViewModelTest {

    private lateinit var setupViewModel: com.sxhardha.setup_module.SetupViewModel
    private lateinit var setupRepository: com.sxhardha.setup_module.SetupRepository
    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        setupRepository = mock()
        val appCoroutineDispatchers = mock<AppCoroutineDispatchers>()
        setupViewModel =
            com.sxhardha.setup_module.SetupViewModel(appCoroutineDispatchers, setupRepository)

        Dispatchers.setMain(mainThreadSurrogate)
    }

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @After
    fun endTest() {
        Dispatchers.resetMain() // reset_counting main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
        print("Test has finished")
    }

    @Test
    fun showErrorLayout_shouldShowOnlyErrorVisible() {
        setupViewModel.showErrorLayout()

        assertError()
    }

    @Test
    fun switchProgressOf_shouldShowContent() {
        setupViewModel.showContent()

        assertContentVisible()
    }

    @Test
    fun switchProgressBarOn_shouldHideErrorAndContent() {
        setupViewModel.switchProgressBarOn()

        setupViewModel.pbVisibility.observeOnce {
            assertEquals(View.VISIBLE, it)
        }

        setupViewModel.errorVisibility.observeOnce {
            assertEquals(View.GONE, it)
        }

        setupViewModel.contentVisibility.observeOnce {
            assertEquals(View.GONE, it)
        }
    }

    private fun assertContentVisible() {
        setupViewModel.pbVisibility.observeOnce {
            assertEquals(View.GONE, it)
        }

        setupViewModel.errorVisibility.observeOnce {
            assertEquals(View.GONE, it)
        }

        setupViewModel.contentVisibility.observeOnce {
            assertEquals(View.VISIBLE, it)
        }
    }

    private fun assertError() {
        setupViewModel.pbVisibility.observeOnce {
            assertEquals(View.GONE, it)
        }

        setupViewModel.errorVisibility.observeOnce {
            assertEquals(View.VISIBLE, it)
        }

        setupViewModel.contentVisibility.observeOnce {
            assertEquals(View.GONE, it)
        }
    }
}