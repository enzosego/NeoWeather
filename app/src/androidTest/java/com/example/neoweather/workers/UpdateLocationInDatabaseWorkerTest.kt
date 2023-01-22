package com.example.neoweather.workers

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import com.example.neoweather.data.workers.location.UpdateLocationInDatabaseWorker
import com.example.neoweather.data.workers.location.LATITUDE_PARAM
import com.example.neoweather.data.workers.location.LONGITUDE_PARAM
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class UpdateLocationInDatabaseWorkerTest {

    private val context: Context = ApplicationProvider.getApplicationContext()
    private lateinit var inputData: Data.Builder

    @Before
    fun setup() {
        inputData = Data.Builder()
        inputData.putDouble(LATITUDE_PARAM, 12.0)
        inputData.putDouble(LONGITUDE_PARAM, 12.0)
        inputData
    }

    @Test
    fun databaseUpdateWorker_withInput_returnsSuccess() = runTest {
        val worker = TestListenableWorkerBuilder<UpdateLocationInDatabaseWorker>(
            context,
            inputData.build()
        ).build()

        val result = worker.doWork()
        result shouldBe ListenableWorker.Result.success(inputData.build())
    }

    @Test
    fun databaseUpdateWorker_noInput_returnsFailure() = runTest {
        val worker = TestListenableWorkerBuilder<UpdateLocationInDatabaseWorker>(
            context
        ).build()

        val result = worker.doWork()
        result shouldBe ListenableWorker.Result.failure()
    }
}