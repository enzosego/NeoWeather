package com.example.neoweather.data.model.day

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "day")
data class Day(
    @PrimaryKey
    val id: Int,
    @ColumnInfo
    val time: String,
    @ColumnInfo
    val sunrise: String,
    @ColumnInfo
    val sunset: String,
    @ColumnInfo(name = "max_temp")
    val maxTemp: String,
    @ColumnInfo(name = "min_temp")
    val minTemp: String,
    @ColumnInfo(name = "precipitation_sum")
    val precipitationSum: String,
    @ColumnInfo(name = "rain_sum")
    val rainSum: String,
    @ColumnInfo(name = "wind_direction_max")
    val windDirectionDominant: String,
    @ColumnInfo(name = "wind_speed_max")
    val windSpeedMax: String,
    @ColumnInfo(name = "weather_code")
    val weatherCode: Int,
)