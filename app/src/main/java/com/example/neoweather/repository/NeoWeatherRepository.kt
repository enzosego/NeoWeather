package com.example.neoweather.repository

import android.text.format.Time.getCurrentTimezone
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.neoweather.data.NeoWeatherDatabase
import com.example.neoweather.data.model.current.CurrentWeather
import com.example.neoweather.data.model.day.DaysEntity
import com.example.neoweather.data.model.hour.HoursEntity
import com.example.neoweather.data.model.place.Place
import com.example.neoweather.data.model.place.isItTimeToUpdate
import com.example.neoweather.data.model.preferences.Preferences
import com.example.neoweather.remote.geocoding.GeoCodingApi
import com.example.neoweather.remote.geocoding.GeoLocation
import com.example.neoweather.remote.geocoding.asDatabaseModel
import com.example.neoweather.remote.reverse_geocoding.ReverseGeoCodingApi
import com.example.neoweather.remote.weather.NeoWeatherApi
import com.example.neoweather.remote.weather.model.NeoWeatherModel
import com.example.neoweather.remote.weather.model.asDatabaseModel
import com.example.neoweather.util.Utils.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.util.*

class NeoWeatherRepository(private val database: NeoWeatherDatabase) {

    val placesList: LiveData<List<Place>> =
        database.placeDao.getAllPlaces().asLiveData()

    var hourlyDataList: LiveData<List<HoursEntity>> =
        database.hoursDao.getAllEntities().asLiveData()

    var dailyDataList: LiveData<List<DaysEntity>> =
        database.daysDao.getAllEntities().asLiveData()

    var currentWeatherList: LiveData<List<CurrentWeather>> =
        database.currentWeatherDao.getAllEntities().asLiveData()

    val preferences: LiveData<Preferences> =
        database.preferencesDao.getPreferences().asLiveData()

    suspend fun refreshPlaceData(location: GeoLocation?, placeId: Int?) {
        if (placeId != null
            && !placesList.value.isNullOrEmpty()
            && !placesList.value!![placeId].isItTimeToUpdate())
            return

        val placeTimezone = getPlaceTimezone(location, placeId)

        val newWeatherInstance =
            NeoWeatherApi.retrofitService.getWeather(
                lat = location?.latitude ?: placesList.value!![placeId!!].latitude,
                long = location?.longitude ?: placesList.value!![placeId!!].longitude,
                timezone = placeTimezone
            )

        updateOrInsertPlace(location, placeId)

        insertWeatherData(
            newWeatherInstance,
            placeTimezone,
            placeId ?: getNewPlaceId())
    }

    private suspend fun insertWeatherData(
        newWeatherInstance: NeoWeatherModel,
        placeTimezone: String,
        placeId: Int
    ) {
        withContext(Dispatchers.IO) {
            database.daysDao.insert(
                newWeatherInstance.dailyForecast.asDatabaseModel(placeId)
            )
            database.hoursDao.insert(
                newWeatherInstance.hourlyForecast.asDatabaseModel(placeTimezone, placeId)
            )
            database.currentWeatherDao.insert(
                newWeatherInstance.currentWeather.asDatabaseModel(placeId)
            )
        }
    }

    private fun getPlaceTimezone(location: GeoLocation?, placeId: Int?)
            : String =
        (location?.timezone ?: "")
            .ifEmpty {
                if (placesList.value.isNullOrEmpty())
                    getCurrentTimezone()
                else
                    placesList.value!![placeId!!].timezone
            }

    suspend fun initiatePreferences() {
        if (preferences.value != null)
            return
        withContext(Dispatchers.IO) {
            database.preferencesDao.insert(
                Preferences(
                    id = 1,
                    isFahrenheitEnabled = false,
                    isInchesEnabled = false,
                    isMilesPerHourEnabled = false
                )
            )
        }
    }

    suspend fun updatePreferences(preferences: Preferences) {
        withContext(Dispatchers.IO) {
            database.preferencesDao.update(preferences)
        }
    }

    private suspend fun updateOrInsertPlace(location: GeoLocation?, placeId: Int?) {
        if (placesList.value.isNullOrEmpty() && location != null)
            insertNewPlace(location, 0)
        else if (location != null && placeId == null)
            insertNewPlace(location, getNewPlaceId())
        else
            insertPlace(placesList.value!![placeId!!]
                .copy(lastUpdateTime = Date.from(Instant.now()).time)
            )
    }

    private suspend fun insertNewPlace(location: GeoLocation, id: Int) {
        withContext(Dispatchers.IO) {
            val placeName = location.name.ifEmpty {
                val address = ReverseGeoCodingApi.retrofitService
                    .getLocationName(
                        location.latitude,
                        location.longitude
                    ).address
                address.city.ifEmpty { address.state }
            }
            val newPlace = GeoCodingApi.retrofitService.getLocation(placeName)
                .results[0]
                .asDatabaseModel(id)

            insertPlace(newPlace)
        }
    }

    private suspend fun insertPlace(place: Place) {
        withContext(Dispatchers.IO) {
            database.placeDao.insert(place)
        }
    }

    suspend fun deletePlace(placeId: Int) {
        withContext(Dispatchers.IO) {
            database.placeDao.delete(placeId)
            database.hoursDao.delete(placeId)
            database.daysDao.delete(placeId)
            database.currentWeatherDao.delete(placeId)

            updateIds(placeId + 1)
        }
    }

    private suspend fun updateIds(placeId: Int) {
        var i = placeId
        withContext(Dispatchers.IO) {
            while (i <= placesList.value!!.size) {
                database.placeDao.updateId(i, i-1)
                database.hoursDao.updateId(i, i-1)
                database.daysDao.updateId(i, i-1)
                database.currentWeatherDao.updateId(i, i-1)
                i++
            }
        }
    }

    private fun getNewPlaceId(): Int =
        placesList.value?.size ?: 0
}