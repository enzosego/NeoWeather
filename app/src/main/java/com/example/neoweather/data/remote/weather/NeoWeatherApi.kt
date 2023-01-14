package com.example.neoweather.data.remote.weather

import com.example.neoweather.data.remote.weather.model.CurrentWeatherParent
import com.example.neoweather.data.remote.weather.model.NeoWeatherModel

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