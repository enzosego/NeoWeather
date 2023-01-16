package com.example.neoweather.domain.model

data class CurrentWeatherModel(
    val time: String,
    val temp: String,
    val windSpeed: String,
    val windDirection: String,
    val weatherDescription: String
)