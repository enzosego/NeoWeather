package com.example.neoweather.model

data class NeoWeatherModel(val city: CityData)

data class CityData(
    val name: String,
    val country: String,
    val population: Int)