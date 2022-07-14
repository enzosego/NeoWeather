package com.example.neoweather.remote.weather.model

import com.example.neoweather.data.model.current.CurrentWeather
import com.squareup.moshi.Json

data class NeoWeatherModel(
    @Json(name = "current_weather")
    val currentWeather: CurrentWeatherData,
    @Json(name = "hourly")
    val hourlyForecast: HourlyForecast,
    @Json(name = "daily")
    val dailyForecast: DailyForecast
)

data class CurrentWeatherData(
    val temperature: Double,
    val time: String,
    @Json(name = "windspeed")
    val windSpeed: Double,
    @Json(name = "winddirection")
    val windDirection: Int,
    @Json(name = "weathercode")
    val weatherCode: Int
)

fun CurrentWeatherData.asDatabaseModel(): CurrentWeather =
    CurrentWeather(
        weatherCode = weatherCode,
        time = time,
        temperature = temperature,
        windDirection = windDirection,
        windSpeed = windSpeed
    )
