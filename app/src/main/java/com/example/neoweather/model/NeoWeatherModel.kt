package com.example.neoweather.model

import com.squareup.moshi.Json

data class NeoWeatherModel(
    @Json(name = "hourly") val forecast: Forecast
)

data class Forecast(
    val time: List<String>,
    @Json(name = "temperature_2m") val hourlyTemp: List<Float>
)