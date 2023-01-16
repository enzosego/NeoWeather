package com.example.neoweather.data.local.model.day

data class Day(
    val time: String,
    val minTemp: Double,
    val maxTemp: Double,
    val precipitationSum: Double,
    val windDirectionDominant: Double,
    val windSpeedMax: Double,
    val weatherDescription: String,
)