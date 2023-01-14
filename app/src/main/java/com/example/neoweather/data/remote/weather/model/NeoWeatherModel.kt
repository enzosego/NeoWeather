package com.example.neoweather.data.remote.weather.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NeoWeatherModel(
    @SerialName("current_weather")
    val currentWeather: CurrentWeatherData,
    @SerialName("hourly")
    val hourlyForecast: HourlyForecast,
    @SerialName("daily")
    val dailyForecast: DailyForecast
)