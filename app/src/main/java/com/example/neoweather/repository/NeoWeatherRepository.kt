package com.example.neoweather.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.neoweather.model.database.NeoWeatherDatabase
import com.example.neoweather.model.database.current.CurrentWeather
import com.example.neoweather.model.database.day.Day
import com.example.neoweather.model.database.hour.Hour
import com.example.neoweather.model.remote.geocoding.GeoCodingApi
import com.example.neoweather.model.remote.weather.NeoWeatherApi
import com.example.neoweather.model.remote.weather.NeoWeatherModel
import com.example.neoweather.model.remote.weather.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NeoWeatherRepository(private val database: NeoWeatherDatabase) {

    val hourlyData: LiveData<List<Hour>> =
        database.hourDao.getAllHours().asLiveData()

    val dailyData: LiveData<List<Day>> =
        database.dayDao.getAllDays().asLiveData()

    val currentWeather: LiveData<CurrentWeather> =
        database.currentWeatherDao.getCurrentWeather().asLiveData()

    suspend fun refreshDatabase() {
        val newLocation = GeoCodingApi.retrofitService.getLocation("santa fe")
            .results[0]

        val newWeatherInstance =
            NeoWeatherApi.retrofitService.getWeather(
                newLocation.latitude,
                newLocation.longitude,
                newLocation.timezone)
        gatherAllData(newWeatherInstance)
    }

    private suspend fun gatherAllData(newWeatherInstance: NeoWeatherModel) {
        val newDayList = newWeatherInstance.dailyForecast.asDatabaseModel()
        val newHourList = newWeatherInstance.hourlyForecast.asDatabaseModel()
        val newCurrentWeather = newWeatherInstance.currentWeather.asDatabaseModel()

        withContext(Dispatchers.IO) {
            database.dayDao.insertAll(newDayList)
            database.hourDao.insertAll(newHourList)
            database.currentWeatherDao.insert(newCurrentWeather)
        }
    }
}