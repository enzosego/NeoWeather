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

    suspend fun refreshDatabase(locationInfo: Map<String, Double>) {
        if (place.value?.lastUpdateTime != null
            && !place.value!!.isItTimeToUpdate())
            return

        val newWeatherInstance =
            NeoWeatherApi.retrofitService.getWeather(
                (locationInfo["latitude"] ?: place.value?.latitude)
                    ?: 31.64,
                (locationInfo["longitude"] ?: place.value?.longitude)
                    ?: 60.70,
                place.value?.timezone ?: "America/Argentina/Cordoba")

        gatherAllData(newWeatherInstance)

        if (locationInfo.isNotEmpty())
            gatherPlaceData(locationInfo)
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

    private suspend fun gatherPlaceData(locationInfo: Map<String, Double>) {
        Log.d(TAG, "Updating data!")
        withContext(Dispatchers.IO) {
            val address = ReverseGeoCodingApi.retrofitService
                .getLocationName(
                    locationInfo["latitude"]!!,
                    locationInfo["longitude"]!!
                ).address
            val placeName = address.city.ifEmpty { address.state }

            val placeInfo = GeoCodingApi.retrofitService
                .getLocation(place.value?.name ?: placeName)
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