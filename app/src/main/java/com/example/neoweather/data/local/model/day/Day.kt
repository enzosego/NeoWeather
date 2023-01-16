package com.example.neoweather.data.local.model.day

import java.util.Date

data class Day(
    val time: Date,
    val minTemp: Double,
    val maxTemp: Double,
    val precipitationSum: Double,
    val windDirectionDominant: Double,
    val windSpeedMax: Double,
    val weatherDescription: String,
)