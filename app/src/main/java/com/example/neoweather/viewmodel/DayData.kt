package com.example.neoweather.viewmodel

data class DayData(
    val time: String,
    val sunrise: String,
    val sunset: String,
    val maxTemp: String,
    val minTemp: String,
    val precipitationSum: String,
    val rainSum: String,
    val windDirectionDominant: String,
    val windSpeedMax: String,
    val weatherCode: Int
)