package com.example.neoweather.remote.weather.model

import com.example.neoweather.data.model.current.CurrentWeather
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherParent(
    @SerialName("current_weather")
    val currentWeather: CurrentWeatherData
)

@Serializable
data class CurrentWeatherData(
    val temperature: Double,
    @SerialName("windspeed")
    val windSpeed: Double,
    @SerialName("winddirection")
    val windDirection: Double,
    @SerialName("weathercode")
    val weatherCode: Int,
    val time: String
)

fun CurrentWeatherData.asDatabaseModel(newId: Int): CurrentWeather =
    CurrentWeather(
        id = newId,
        temperature = temperature,
        windSpeed = windSpeed,
        windDirection = windDirection,
        weatherCode = weatherCode,
        time = time
    )