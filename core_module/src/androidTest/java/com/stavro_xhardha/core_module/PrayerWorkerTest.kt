package com.stavro_xhardha.core_module

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.stavro_xhardha.core_module.background.PrayerTimeWorkManager
import com.stavro_xhardha.core_module.dependency_injection.CoreApplication
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PrayerWorkerTest {
    private lateinit var context: Context

    @Before
    fun start() {
        context = ApplicationProvider.getApplicationContext() as CoreApplication
    }

    @After
    fun end() {

    }

    @Test
    fun testWorkerExecution() {
        val prayerWorker =
            TestListenableWorkerBuilder<PrayerTimeWorkManager>((context)).build()
        runBlocking {
            val result = prayerWorker.doWork()
            assertThat(result, `is`(ListenableWorker.Result.success()))
        }
    }
}