package com.example.neoweather.model

import com.squareup.moshi.Json

data class ForecastWeatherModel(
    val list: List<Forecast>,
    val city: City)

data class Forecast(
    val main: Temperature,
    val weather: List<Weather>,
    @Json(name = "pop") val precipitation: Int
)

data class City(
    val name: String,
    val country: String,
    val population: Int
)
