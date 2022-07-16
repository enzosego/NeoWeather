package com.example.neoweather.data.model.place

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "place")
data class Place(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo
    val name: String,
    @ColumnInfo
    val latitude: Double,
    @ColumnInfo
    val longitude: Double,
    @ColumnInfo(name = "country_code")
    val countryCode: String,
    @ColumnInfo
    val country: String,
    @ColumnInfo
    val timezone: String
)