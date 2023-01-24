package com.example.neoweather.data.remote.weather.model

import com.example.neoweather.data.local.model.current.CurrentWeather
import com.example.neoweather.data.local.model.WeatherCodeMapping
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
        time = time,
        temperature = temperature,
        windSpeed = windSpeed.toString(),
        windDirection = windDirection.toString(),
        weatherDescription = WeatherCodeMapping.getDescription(weatherCode)
    )