package com.example.neoweather.data.repository

import com.example.neoweather.data.local.NeoWeatherDatabase
import com.example.neoweather.data.local.model.current.CurrentWeather
import com.example.neoweather.data.local.model.day.DaysEntity
import com.example.neoweather.data.local.model.hour.HoursEntity
import com.example.neoweather.data.local.model.place.canUpdate
import com.example.neoweather.data.local.model.place.newLastUpdateTime
import com.example.neoweather.data.remote.weather.NeoWeatherApi
import com.example.neoweather.data.remote.weather.model.NeoWeatherModel
import com.example.neoweather.data.remote.weather.model.asDatabaseModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class WeatherRepository(
    private val database: NeoWeatherDatabase,
    private val placesRepository: PlacesRepository,
    private val weatherSource: NeoWeatherApi,
    private val ioDispatcher: CoroutineDispatcher
) {

    val currentWeatherFlow: Flow<List<CurrentWeather>> =
        database.currentWeatherDao.getAllEntities()

    var currentWeatherList: List<CurrentWeather> = emptyList()

    var hourlyDataFlow: Flow<List<HoursEntity>> =
        database.hoursDao.getAllEntities()

    var hourlyDataList: List<HoursEntity> = emptyList()

    var dailyDataFlow: Flow<List<DaysEntity>> =
        database.daysDao.getAllEntities()

    var dailyDataList: List<DaysEntity> = emptyList()

    init {
        collectAllValues()
    }

    suspend fun refreshWeatherAtLocation(id: Int) {
        val place = placesRepository.placesList[id]
        if (!place.canUpdate())
            return

        with(place) {
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
            placesRepository.savePlaceInDatabase(copy(lastUpdateTime = newLastUpdateTime()))
        }
    }

    private suspend fun insertWeatherData(
        weather: NeoWeatherModel,
        id: Int
    ) =
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

    private fun collectAllValues() {
        currentWeatherFlow.collectValue(ioDispatcher) { currentWeatherList = it }
        hourlyDataFlow.collectValue(ioDispatcher) { hourlyDataList = it }
        dailyDataFlow.collectValue(ioDispatcher) { dailyDataList = it }
    }
}