package com.example.neoweather.remote.weather.model

import com.squareup.moshi.Json

data class NeoWeatherModel(
    @Json(name = "current_weather")
    val currentWeather: CurrentWeatherData,
    @Json(name = "hourly")
    val hourlyForecast: HourlyForecast,
    @Json(name = "daily")
    val dailyForecast: DailyForecast
)