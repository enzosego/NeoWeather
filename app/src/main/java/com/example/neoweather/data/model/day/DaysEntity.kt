package com.example.neoweather.data.model.day

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "days")
data class DaysEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "day_list")
    val dayList: List<Day>
)