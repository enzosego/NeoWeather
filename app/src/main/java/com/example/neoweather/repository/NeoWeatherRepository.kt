package com.example.neoweather.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.neoweather.data.NeoWeatherDatabase
import com.example.neoweather.data.model.current.CurrentWeather
import com.example.neoweather.data.model.day.DaysEntity
import com.example.neoweather.data.model.hour.HoursEntity
import com.example.neoweather.data.model.place.Place
import com.example.neoweather.data.model.place.isItTimeToUpdate
import com.example.neoweather.data.model.place.newLastUpdateTime
import com.example.neoweather.data.model.preferences.Preferences
import com.example.neoweather.remote.geocoding.GeoCodingApi
import com.example.neoweather.remote.geocoding.GeoLocation
import com.example.neoweather.remote.geocoding.asDatabaseModel
import com.example.neoweather.remote.reverse_geocoding.ReverseGeoCodingApi
import com.example.neoweather.remote.reverse_geocoding.getPlaceName
import com.example.neoweather.remote.weather.NeoWeatherApi
import com.example.neoweather.remote.weather.model.NeoWeatherModel
import com.example.neoweather.remote.weather.model.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    suspend fun refreshPlaceData(placeId: Int) {
        if (!placesList.value!![placeId].isItTimeToUpdate())
            return

        val currentPlace = placesList.value!![placeId]

        val newWeatherInstance = NeoWeatherApi.retrofitService.getWeather(
            currentPlace.latitude,
            currentPlace.longitude,
            currentPlace.timezone
        )
        insertWeatherData(
            newWeatherInstance,
            currentPlace.timezone,
            placeId)
        insertPlace(currentPlace.copy(
            lastUpdateTime = currentPlace.newLastUpdateTime())
        )
    }

    suspend fun updateOrInsertPlace(location: GeoLocation) {
        initiatePreferences()

        if (location.name != "")
            insertNewPlace(location)
        else
            getPlaceFromGpsLocation(location)
    }

    private suspend fun getPlaceFromGpsLocation(location: GeoLocation) {
        withContext(Dispatchers.IO) {
            val address = ReverseGeoCodingApi.retrofitService
                .getLocationName(location.latitude, location.longitude)
                .address

            val places = database.placeDao
                .getMatchingPlace(
                    address.getPlaceName(),
                    address.country
                )

            if (places.isEmpty())
                insertNewPlace(location)
            else
                insertPlace(
                    places.first().copy(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                )
        }
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

    private suspend fun initiatePreferences() {
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

    private suspend fun insertNewPlace(location: GeoLocation) {
        withContext(Dispatchers.IO) {
            val placeName = location.name.ifEmpty {
                val address = ReverseGeoCodingApi.retrofitService
                    .getLocationName(
                        location.latitude,
                        location.longitude
                    ).address
                address.getPlaceName()
            }
            val newPlace = GeoCodingApi.retrofitService.getLocation(placeName)
                .results[0]
                .asDatabaseModel(getNewPlaceId())

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