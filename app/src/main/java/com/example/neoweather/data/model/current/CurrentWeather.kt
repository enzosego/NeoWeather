package com.example.neoweather.data.model.current

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_weather")
data class CurrentWeather(
    @PrimaryKey
    val id: Int,
    @ColumnInfo
    val time: String,
    @ColumnInfo
    val temperature: Double,
    @ColumnInfo(name = "wind_speed")
    val windSpeed: Double,
    @ColumnInfo(name = "wind_direction")
    val windDirection: Int,
    @ColumnInfo(name = "weather_code")
    val weatherCode: Int,
)