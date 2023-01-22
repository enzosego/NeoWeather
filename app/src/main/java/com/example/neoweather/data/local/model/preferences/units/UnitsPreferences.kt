package com.example.neoweather.data.local.model.preferences.units

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "units_preferences")
data class UnitsPreferences(
    @PrimaryKey @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "is_fahrenheit_enabled")
    val isFahrenheitEnabled: Boolean,
    @ColumnInfo(name = "is_inches_enabled")
    val isInchesEnabled: Boolean,
    @ColumnInfo(name = "is_miles_enabled")
    val isMilesEnabled: Boolean,
)