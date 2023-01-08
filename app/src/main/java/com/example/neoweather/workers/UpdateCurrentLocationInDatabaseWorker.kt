package com.example.neoweather.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.neoweather.data.NeoWeatherDatabase
import com.example.neoweather.remote.geocoding.model.GeoLocation
import com.example.neoweather.repository.NeoWeatherRepository
import com.example.neoweather.workers.NotificationUtils.LATITUDE_PARAM
import com.example.neoweather.workers.NotificationUtils.LONGITUDE_PARAM

class UpdateCurrentLocationInDatabaseWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {

        val latitude = workerParams.inputData.getDouble(LATITUDE_PARAM, 0.0)
        val longitude = workerParams.inputData.getDouble(LONGITUDE_PARAM, 0.0)
        if (latitude == 0.0 && longitude == 0.0) {
            Log.d("update_database_worker", "Wrong inputs")
            return Result.failure()
        }

        val database = NeoWeatherDatabase.getDatabase(context)
        val repository = NeoWeatherRepository(database)

        val geoLocation = makeGeoLocationFromInput(latitude, longitude)

        repository.updateOrInsertPlace(geoLocation)

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