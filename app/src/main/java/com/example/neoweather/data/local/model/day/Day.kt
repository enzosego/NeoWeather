package com.example.neoweather.data.local.model.day

data class Day(
    val time: String,
    val maxTemp: Double,
    val minTemp: Double,
    val precipitationSum: Double,
    val windDirectionDominant: Double,
    val windSpeedMax: Double,
    val weatherCode: Int,
)