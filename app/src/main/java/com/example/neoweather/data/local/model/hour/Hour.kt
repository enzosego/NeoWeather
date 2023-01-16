package com.example.neoweather.data.local.model.hour

import java.util.Date

data class Hour(
    val time: Date,
    val temp: Double,
    val weatherDescription: String
)