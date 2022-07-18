package com.example.neoweather.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.neoweather.data.NeoWeatherDatabase
import com.example.neoweather.data.model.current.CurrentWeather
import com.example.neoweather.data.model.day.Day
import com.example.neoweather.data.model.hour.Hour
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

    val hourlyData: LiveData<List<Hour>> =
        database.hourDao.getAllHours().asLiveData()

    val dailyData: LiveData<List<Day>> =
        database.dayDao.getAllDays().asLiveData()

    val preferences: LiveData<Preferences> =
        database.preferencesDao.getPreferences().asLiveData()

    val currentWeather: LiveData<CurrentWeather> =
        database.currentWeatherDao.getCurrentWeather().asLiveData()

    val place: LiveData<Place> =
        database.placeDao.getPlace().asLiveData()

    suspend fun refreshDatabase(location: GeoLocation?) {
        if (place.value?.lastUpdateTime != null
            && !place.value!!.isItTimeToUpdate()
            && (location?.name ?: "").isEmpty())
            return

        /*
        TODO: Fix timezone bug -
         If you select a location from different timezone than your phone the times for
         the hourly data will be wrong
         */
        val newWeatherInstance =
            NeoWeatherApi.retrofitService.getWeather(
                lat = (location?.latitude ?: place.value?.latitude)
                    ?: 31.64,
                long = (location?.longitude ?: place.value?.longitude)
                    ?: 60.70,
                timezone = (location?.timezone?.ifEmpty { place.value?.timezone } )
                    ?: "America/Argentina/Cordoba")
        Log.d(TAG, "Updating data!")

        gatherAllData(newWeatherInstance)

        if (location != null)
            refreshPlaceData(location)
    }

    private suspend fun gatherAllData(newWeatherInstance: NeoWeatherModel) {
        val newDayList = newWeatherInstance.dailyForecast.asDatabaseModel()
        val newHourList = newWeatherInstance.hourlyForecast.asDatabaseModel()
        val newCurrentWeather = newWeatherInstance.currentWeather.asDatabaseModel()

        withContext(Dispatchers.IO) {
            if (preferences.value == null)
                initiatePreferences()

            database.dayDao.insertAll(newDayList)
            database.hourDao.insertAll(newHourList)
            database.currentWeatherDao.insert(newCurrentWeather)
        }
    }

    private suspend fun initiatePreferences() {
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

    private suspend fun refreshPlaceData(location: GeoLocation) {
        withContext(Dispatchers.IO) {
            val placeName = location.name.ifEmpty {
                val address = ReverseGeoCodingApi.retrofitService
                    .getLocationName(
                        location.latitude,
                        location.longitude
                    ).address
                address.city.ifEmpty { address.state }
            }

            val placeInfo = GeoCodingApi.retrofitService.getLocation(placeName)
                .results[0]

            updatePlace(placeInfo.asDatabaseModel())
        }
    }

    private suspend fun updatePlace(place: Place) {
        withContext(Dispatchers.IO) {
            database.placeDao.insert(place)
        }
    }
}