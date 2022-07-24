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
        /*
        if (placeId != null
            && !placesList.value.isNullOrEmpty()
            && !placesList.value!![placeId].isItTimeToUpdate())
            return
         */

        val placeTimezone =
            (location?.timezone ?: "")
                .ifEmpty {
                    if (placesList.value.isNullOrEmpty())
                        getCurrentTimezone()
                    else
                        placesList.value!![placeId!!].timezone
                }

        val newWeatherInstance =
            NeoWeatherApi.retrofitService.getWeather(
                lat = location?.latitude ?: placesList.value!![placeId!!].latitude,
                long = location?.longitude ?: placesList.value!![placeId!!].longitude,
                timezone = placeTimezone
            )
        val newPlaceId = getNewPlaceId()

        if (placesList.value.isNullOrEmpty() && location != null)
            insertNewPlace(location, 0)
        else if (location != null && placeId == null)
            insertNewPlace(location, newPlaceId)

        insertWeatherData(
            newWeatherInstance,
            placeTimezone,
            placeId ?: newPlaceId)
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

            updatePlace(newPlace.asDatabaseModel(id))
        }
    }

    private suspend fun updatePlace(place: Place) {
        withContext(Dispatchers.IO) {
            database.placeDao.insert(place)
        }
    }

    private fun getNewPlaceId(): Int =
        if (placesList.value.isNullOrEmpty())
            0
        else
            placesList.value!!.size
}