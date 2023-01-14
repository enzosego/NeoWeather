package com.example.neoweather.data.local.model.hour

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hours")
data class HoursEntity (
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "hour_list")
    val hourList: List<Hour>
)