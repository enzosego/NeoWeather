package com.example.neoweather.remote.weather

import com.example.neoweather.remote.weather.model.NeoWeatherModel

interface NeoWeatherApi {
    suspend fun getWeather(
        lat: Double,
        long: Double,
        timezone: String
    ): NeoWeatherModel
}