package com.example.neoweather.data.workers.location

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.neoweather.data.remote.geocoding.model.GeoLocation
import com.example.neoweather.data.repository.WeatherDataRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

const val UPDATE_LOCATION_WORK_NAME = "update_location_work_name"

class UpdateLocationInDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val weatherDataRepository: WeatherDataRepository by inject()

    override suspend fun doWork(): Result {

        /*
        val latitude = inputData.getDouble(LATITUDE_PARAM, 0.0)
        val longitude = inputData.getDouble(LONGITUDE_PARAM, 0.0)
         */
        val locationId = inputData.getInt(LOCATION_ID_PARAM, -1)
        if (locationId == -1) {
            Log.d("update_database_worker", "Wrong input")
            return Result.failure()
        }
        val location = weatherDataRepository.placesList.value!![locationId]
        val geoLocation = makeGeoLocationFromInput(location.latitude, location.longitude)

        weatherDataRepository.updateOrInsertPlace(geoLocation)

        /*
        val output = workDataOf(
            LOCATION_ID_PARAM to locationId
        )
         */
        return Result.success(inputData)
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