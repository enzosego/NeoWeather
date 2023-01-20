package com.example.neoweather.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.neoweather.data.local.NeoWeatherDatabase
import com.example.neoweather.data.local.model.current.CurrentWeather
import com.example.neoweather.data.local.model.day.DaysEntity
import com.example.neoweather.data.local.model.hour.HoursEntity
import com.example.neoweather.data.local.model.place.Place
import com.example.neoweather.data.local.model.place.canUpdate
import com.example.neoweather.data.local.model.place.newLastUpdateTime
import com.example.neoweather.data.remote.geocoding.GeoCodingApi
import com.example.neoweather.data.remote.geocoding.model.GeoLocation
import com.example.neoweather.data.remote.geocoding.model.asDatabaseModel
import com.example.neoweather.data.remote.geocoding.model.isGpsLocation
import com.example.neoweather.data.remote.reverse_geocoding.ReverseGeoCodingApi
import com.example.neoweather.data.remote.reverse_geocoding.model.getName
import com.example.neoweather.data.remote.weather.NeoWeatherApi
import com.example.neoweather.data.remote.weather.model.NeoWeatherModel
import com.example.neoweather.data.remote.weather.model.asDatabaseModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class WeatherDataRepository(
    private val database: NeoWeatherDatabase,
    private val weatherSource: NeoWeatherApi,
    private val geoCodingSource: GeoCodingApi,
    private val reverseGeoCodingSource: ReverseGeoCodingApi,
    private val ioDispatcher: CoroutineDispatcher
) {

    val placesList: LiveData<List<Place>> =
        database.placeDao.getAllPlaces().asLiveData()

    var hourlyDataList: LiveData<List<HoursEntity>> =
        database.hoursDao.getAllEntities().asLiveData()

    var dailyDataList: LiveData<List<DaysEntity>> =
        database.daysDao.getAllEntities().asLiveData()

    var currentWeatherList: LiveData<List<CurrentWeather>> =
        database.currentWeatherDao.getAllEntities().asLiveData()

    suspend fun refreshPlaceWeather(id: Int) {
        if (placesList.value!![id].canUpdate(30))
            return

        with(placesList.value!![id]) {
            val weather = weatherSource
                .getWeather(
                    latitude,
                    longitude,
                    timezone
                )
            insertWeatherData(
                weather,
                id
            )
            savePlaceInDatabase(copy(lastUpdateTime = newLastUpdateTime()))
        }
    }

    private suspend fun insertWeatherData(
        weather: NeoWeatherModel,
        id: Int
    ) {
        withContext(ioDispatcher) {
            database.daysDao.insert(
                weather.dailyForecast.asDatabaseModel(id)
            )
            database.hoursDao.insert(
                weather.hourlyForecast.asDatabaseModel(id)
            )
            database.currentWeatherDao.insert(
                weather.currentWeather.asDatabaseModel(id)
            )
        }
    }

    suspend fun updateOrInsertPlace(location: GeoLocation) {
        if (location.isGpsLocation())
            getPlaceFromGps(location)
        else
            insertNewPlace(location)
    }

    private suspend fun getPlaceFromGps(location: GeoLocation) {
        withContext(ioDispatcher) {
            val places = database.placeDao.matchGpsLocation()

            if (places.isEmpty()) {
                increaseIds()
                insertNewPlace(location, id = 0)
            }
            else
                savePlaceInDatabase(
                    places.first().copy(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                )
        }
    }

    private suspend fun insertNewPlace(location: GeoLocation, id: Int? = null) {
        withContext(ioDispatcher) {
            val placeName = location.name.ifEmpty {
                reverseGeoCodingSource
                    .getLocationName(
                        location.latitude,
                        location.longitude)
                    .address
                    .getName()
            }
            val place = geoCodingSource.getLocation(placeName)
                .results[0]
                .asDatabaseModel(id ?: getNewPlaceId(), location.isGpsLocation())

            savePlaceInDatabase(place)
        }
    }

    private suspend fun savePlaceInDatabase(place: Place) {
        withContext(ioDispatcher) {
            database.placeDao.insert(place)
        }
    }

    suspend fun deletePlace(placeId: Int) {
        withContext(ioDispatcher) {
            database.placeDao.delete(placeId)
            database.hoursDao.delete(placeId)
            database.daysDao.delete(placeId)
            database.currentWeatherDao.delete(placeId)

            decreaseIds(placeId + 1)
        }
    }

    private suspend fun decreaseIds(placeId: Int) {
        withContext(ioDispatcher) {
            for (i in placeId..placesList.value!!.size) {
                database.placeDao.updateId(i, i-1)
                database.hoursDao.updateId(i, i-1)
                database.daysDao.updateId(i, i-1)
                database.currentWeatherDao.updateId(i, i-1)
            }
        }
    }

    private suspend fun increaseIds() {
        withContext(ioDispatcher) {
            for (i in placesList.value!!.size downTo 0) {
                database.placeDao.updateId(i, i+1)
                database.hoursDao.updateId(i, i+1)
                database.daysDao.updateId(i, i+1)
                database.currentWeatherDao.updateId(i, i+1)
            }
        }
    }

    private fun getNewPlaceId(): Int =
        placesList.value?.size ?: 0
}