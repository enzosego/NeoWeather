package com.example.neoweather.data.model.preferences

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preferences")
data class Preferences(
    @PrimaryKey
    val id: Int,
    @ColumnInfo
    val isFahrenheitEnabled: Boolean,
    @ColumnInfo
    val isInchesEnabled: Boolean,
    @ColumnInfo
    val isMilesPerHourEnabled: Boolean,
)