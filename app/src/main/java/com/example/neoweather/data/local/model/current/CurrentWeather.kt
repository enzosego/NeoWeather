package com.example.neoweather.data.local.model.current

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_weather")
data class CurrentWeather(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "time")
    val time: String,
    @ColumnInfo(name = "temperature")
    val temperature: Double,
    @ColumnInfo(name = "wind_speed")
    val windSpeed: String,
    @ColumnInfo(name = "wind_direction")
    val windDirection: String,
    @ColumnInfo(name = "weather_description")
    val weatherDescription: String
)