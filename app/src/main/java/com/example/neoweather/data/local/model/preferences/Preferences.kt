package com.example.neoweather.data.local.model.preferences

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preferences")
data class Preferences(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "is_fahrenheit_enabled")
    val isFahrenheitEnabled: Boolean,
    @ColumnInfo(name = "is_inches_enabled")
    val isInchesEnabled: Boolean,
    @ColumnInfo(name = "is_miles_enabled")
    val isMilesEnabled: Boolean,
    @ColumnInfo(name = "are_notifications_enabled")
    val areNotificationsEnabled: Boolean,
    @ColumnInfo(name = "notifications_interval")
    val notificationsInterval: Long,
    @ColumnInfo(name = "background_permission_denied")
    val backgroundPermissionDenied: Boolean
)