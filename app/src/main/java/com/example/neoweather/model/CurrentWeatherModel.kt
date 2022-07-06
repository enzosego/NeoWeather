package com.example.neoweather.model

import com.squareup.moshi.Json

data class CurrentWeatherModel(
    val weather: List<Weather>,
    val main: Temperature,
    val name: String,
    @Json(name = "sys") val additionalInfo: AdditionalInfo)

data class Temperature(
    val temp: Double,
    @Json(name = "feels_like") val feelsLike: Double,
    @Json(name = "temp_min") val tempMin: Double,
    @Json(name = "temp_max") val tempMax: Double,
    val pressure: Int,
    val humidity: Int
)

data class Weather(
    val main: String,
    val description: String)

data class AdditionalInfo(val country: String)