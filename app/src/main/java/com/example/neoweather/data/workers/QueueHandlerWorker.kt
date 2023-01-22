package com.example.neoweather.data.workers

import android.content.Context
import androidx.work.*
import com.example.neoweather.data.repository.PreferencesRepository
import com.example.neoweather.data.repository.WeatherDataRepository
import com.example.neoweather.data.workers.location.*
import com.example.neoweather.data.workers.weather.UpdateWeatherInDatabaseWorker
import com.example.neoweather.ui.isBackgroundPermissionGranted
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

const val QUEUE_HANDLER_WORK_TAG = "handler_work"
const val QUEUE_HANDLER_WORK_NAME = "handler_worker"

class QueueHandlerWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams), KoinComponent {

    private val preferencesRepository: PreferencesRepository by inject()
    private val weatherDataRepository: WeatherDataRepository by inject()

    private lateinit var workManager: WorkManager

    override fun doWork(): Result {

        val preferredLocationId =
            preferencesRepository.dataPreferences.value?.preferredLocationId ?: 0

        val preferredLocation =
            weatherDataRepository.placesList.value?.getOrNull(preferredLocationId)
                ?: return Result.failure()

        workManager = WorkManager.getInstance(context)

        val interval = preferencesRepository.dataPreferences.value?.updateInterval ?: 1L

        if (preferredLocation.isGpsLocation && isBackgroundPermissionGranted(context))
            enqueueGetLocationRequest(
                makeConstraints(),
                interval
            )
        else
            enqueueWeatherUpdateRequest(
                createInputData(preferredLocationId),
                makeConstraints(),
                interval
            )

        return Result.success()
    }

    private fun enqueueGetLocationRequest(constraints: Constraints, updateInterval: Long) {
        val getCurrentLocationRequest =
            PeriodicWorkRequestBuilder<GetCurrentLocationWorker>(
                updateInterval,
                TimeUnit.HOURS
            )
                .addTag(GET_CURRENT_LOCATION_WORK_TAG)
                .setConstraints(constraints)
                .build()

        workManager.enqueueUniquePeriodicWork(
            GET_CURRENT_LOCATION_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            getCurrentLocationRequest
        )
    }

    private fun enqueueWeatherUpdateRequest(
        inputData: Data,
        networkConstraints: Constraints,
        updateInterval: Long
    ) {
        val updateWeatherRequest =
            PeriodicWorkRequestBuilder<UpdateWeatherInDatabaseWorker>(
                updateInterval,
                TimeUnit.HOURS
            )
                .setInputData(inputData)
                .setConstraints(networkConstraints)
                .build()

        workManager
            .enqueueUniquePeriodicWork(
                UPDATE_LOCATION_WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                updateWeatherRequest
            )
    }

    private fun makeConstraints(): Constraints =
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

    private fun createInputData(locationId: Int): Data =
        Data.Builder()
            .putInt(LOCATION_ID_PARAM, locationId)
            .build()
}