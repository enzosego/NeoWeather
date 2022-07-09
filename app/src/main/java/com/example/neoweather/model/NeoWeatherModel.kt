package com.example.neoweather.model

import com.squareup.moshi.Json

data class NeoWeatherModel(
    @Json(name = "current_weather") val currentWeather: CurrentWeather,
    @Json(name = "hourly") val hourlyForecast: HourlyForecast,
    @Json(name = "daily") val dailyForecast: DailyForecast
)

data class CurrentWeather(
    val temperature: Float,
    @Json(name = "windspeed") val windSpeed: Double,
    @Json(name = "winddirection") val windDirection: Int,
    @Json(name = "weathercode") val weatherCode: Int,
    val time: String
)

data class HourlyForecast(
    val time: List<String>,
    @Json(name = "temperature_2m") val hourlyTemp: List<Double>,
    @Json(name = "weathercode") val weatherCode: List<Int>
)

data class DailyForecast(
    val time: List<String>,
    val sunrise: List<String>,
    val sunset: List<String>,
    @Json(name = "temperature_2m_max") val maxTemp: List<Double>,
    @Json(name = "temperature_2m_min") val minTemp: List<Double>,
    @Json(name = "precipitation_sum") val precipitationSum: List<Double>,
    @Json(name = "rain_sum") val rainSum: List<Double>,
    @Json(name = "winddirection_10m_dominant") val windDirectionDominant: List<Double>,
    @Json(name = "windspeed_10m_max") val windSpeedMax: List<Double>,
    @Json(name = "weathercode") val weatherCode: List<Int>
)