package com.example.neoweather.model.database.current

import androidx.room.*

@Entity(tableName = "current_data")
class CurrentWeather(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
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