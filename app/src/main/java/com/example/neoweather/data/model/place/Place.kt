package com.example.neoweather.data.model.place

import androidx.room.*
import com.example.neoweather.util.Utils.getTimeDiffInMinutes
import java.time.Instant
import java.util.*

@Entity(tableName = "place")
data class Place(
    @PrimaryKey
    val id: Int,
    @ColumnInfo
    val name: String,
    @ColumnInfo
    val latitude: Double,
    @ColumnInfo
    val longitude: Double,
    @ColumnInfo
    val country: String,
    @ColumnInfo(name = "country_code")
    val countryCode: String,
    @ColumnInfo
    val timezone: String,
    @ColumnInfo(name = "last_update_time")
    val lastUpdateTime: Long
)

fun Place.isItTimeToUpdate(): Boolean =
    getTimeDiffInMinutes(lastUpdateTime) >= 30
