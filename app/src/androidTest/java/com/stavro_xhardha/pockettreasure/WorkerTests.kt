package com.stavro_xhardha.pockettreasure

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.stavro_xhardha.pockettreasure.background.QuranWorker
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class WorkerTests {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testWorkerCall() {
        val quranWorker = TestListenableWorkerBuilder<QuranWorker>(context).build()
        runBlocking {
            val result = quranWorker.doWork()
            assertThat(result, `is`(ListenableWorker.Result.success()))
        }
    }

}