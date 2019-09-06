package com.stavro_xhardha.pockettreasure.setup_pkg

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.nhaarman.mockitokotlin2.mock
import com.stavro_xhardha.pockettreasure.brain.observeOnce
import com.stavro_xhardha.pockettreasure.ui.setup.SetupRepository
import com.stavro_xhardha.pockettreasure.ui.setup.SetupViewModel
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

    private lateinit var setupViewModel: SetupViewModel
    private lateinit var setupRepository: SetupRepository
    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        setupRepository = mock()
        val savedStateHandle = mock<SavedStateHandle>()
        setupViewModel = SetupViewModel(setupRepository, savedStateHandle)

        Dispatchers.setMain(mainThreadSurrogate)
    }

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @After
    fun endTest() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
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