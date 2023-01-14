package com.example.neoweather.workers

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.example.neoweather.data.workers.NotificationUtils
import com.example.neoweather.data.workers.ShowCurrentWeatherNotificationWorker
import io.kotest.matchers.shouldBe
import io.ktor.http.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class ShowCurrentWeatherNotificationWorkerTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private lateinit var inputData: Data.Builder

    @Before
    fun setup() {
        inputData = Data.Builder()
        inputData.putDouble(NotificationUtils.LATITUDE_PARAM, 12.0)
        inputData.putDouble(NotificationUtils.LONGITUDE_PARAM, 12.0)
    }

    @Test
    fun notificationWorker_withInput_returnsSuccess() = runTest {
        val worker = TestListenableWorkerBuilder<ShowCurrentWeatherNotificationWorker>(
                context,
                inputData.build()
            ).build()

        val result = worker.doWork()
        result shouldBe ListenableWorker.Result.success()
    }

    @Test
    fun notificationWorker_noInput_returnsFailure() = runTest {
        val worker = TestListenableWorkerBuilder<ShowCurrentWeatherNotificationWorker>(
            context
        ).build()

        val result = worker.doWork()
        result shouldBe ListenableWorker.Result.failure()
    }
}