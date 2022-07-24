package com.example.neoweather.data.model.day

data class Day(
    val time: String,
    val sunrise: String,
    val sunset: String,
    val maxTemp: Double,
    val minTemp: Double,
    val precipitationSum: Double,
    val rainSum: Double,
    val windDirectionDominant: Double,
    val windSpeedMax: Double,
    val weatherCode: Int,
)