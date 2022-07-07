package com.example.neoweather.model

import com.squareup.moshi.Json

data class NeoWeatherModel(
    @Json(name = "current_weather") val currentWeather: CurrentWeather,
    @Json(name = "hourly") val forecast: Forecast
)

data class CurrentWeather(
    val temperature: Float,
    @Json(name = "windspeed") val windSpeed: Float,
    @Json(name = "winddirection") val windDirection: Int,
    @Json(name = "weathercode") val weatherCode: Int,
    val time: String
)

data class Forecast(
    val time: List<String>,
    @Json(name = "temperature_2m") val hourlyTemp: List<Float>
)