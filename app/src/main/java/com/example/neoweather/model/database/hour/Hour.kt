package com.example.neoweather.model.database.hour

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hour")
data class Hour(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo
    val time: String,
    @ColumnInfo
    val temp: String,
    @ColumnInfo
    val weatherCode: Int,
)