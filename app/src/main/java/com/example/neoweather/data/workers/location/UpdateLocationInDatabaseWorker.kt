package com.example.neoweather.data.workers.location

import android.content.Context
import androidx.work.*
import com.example.neoweather.data.remote.geocoding.model.GeoLocation
import com.example.neoweather.data.repository.WeatherDataRepository

const val UPDATE_LOCATION_WORK_NAME = "update_location_work_name"

class UpdateLocationInDatabaseWorker(
    private val weatherDataRepository: WeatherDataRepository,
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {


    override suspend fun doWork(): Result {

        weatherDataRepository.updateOrInsertPlace(inputData.makeGeoLocation())

        return Result.success()
    }

    private fun Data.makeGeoLocation(): GeoLocation =
        GeoLocation(
            name = "",
            latitude = getDouble(LATITUDE_PARAM, 0.0),
            longitude = getDouble(LONGITUDE_PARAM, 0.0),
            countryCode = "",
            country = "",
            timezone = ""
        )
}