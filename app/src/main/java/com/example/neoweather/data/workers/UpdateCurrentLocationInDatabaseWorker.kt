package com.example.neoweather.data.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.neoweather.data.remote.geocoding.model.GeoLocation
import com.example.neoweather.data.repository.WeatherDataRepository
import com.example.neoweather.data.workers.NotificationUtils.LATITUDE_PARAM
import com.example.neoweather.data.workers.NotificationUtils.LONGITUDE_PARAM
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UpdateCurrentLocationInDatabaseWorker(
    context: Context,
    private val workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val weatherDataRepository: WeatherDataRepository by inject()

    override suspend fun doWork(): Result {

        val latitude = workerParams.inputData.getDouble(LATITUDE_PARAM, 0.0)
        val longitude = workerParams.inputData.getDouble(LONGITUDE_PARAM, 0.0)
        if (latitude == 0.0 && longitude == 0.0) {
            Log.d("update_database_worker", "Wrong inputs")
            return Result.failure()
        }
        val geoLocation = makeGeoLocationFromInput(latitude, longitude)

        weatherDataRepository.updateOrInsertPlace(geoLocation)

        val output = workDataOf(
            LATITUDE_PARAM to latitude,
            LONGITUDE_PARAM to longitude
        )
        return Result.success(output)
    }

    private fun makeGeoLocationFromInput(lat: Double, lon: Double): GeoLocation =
        GeoLocation(
            name = "",
            latitude = lat,
            longitude = lon,
            countryCode = "",
            country = "",
            timezone = ""
        )
}