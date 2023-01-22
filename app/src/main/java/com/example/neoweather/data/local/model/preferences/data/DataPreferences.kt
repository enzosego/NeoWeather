package com.example.neoweather.data.local.model.preferences.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_preferences")
data class DataPreferences(
    @PrimaryKey @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "update_in_background")
    val updateInBackground: Boolean,
    @ColumnInfo(name = "update_interval")
    val updateInterval: Long,
    @ColumnInfo(name = "background_permission_denied")
    val backgroundPermissionDenied: Boolean,
    @ColumnInfo(name = "are_notifications_enabled")
    val areNotificationsEnabled: Boolean,
    @ColumnInfo(name = "preferred_location_id")
    val preferredLocationId: Int
)