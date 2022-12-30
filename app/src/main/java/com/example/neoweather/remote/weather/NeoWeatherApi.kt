package com.example.neoweather.remote.weather

import com.example.neoweather.remote.weather.model.CurrentWeatherData
import com.example.neoweather.remote.weather.model.CurrentWeatherParent
import com.example.neoweather.remote.weather.model.NeoWeatherModel

interface NeoWeatherApi {
    suspend fun getWeather(
        lat: Double,
        lon: Double,
        timezone: String
    ): NeoWeatherModel

    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double
    ): CurrentWeatherParent
}