package com.example.neoweather.data.local.model.place

import androidx.room.*
import java.util.*

@Entity(tableName = "place")
data class Place(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "latitude")
    val latitude: Double,
    @ColumnInfo(name = "longitude")
    val longitude: Double,
    @ColumnInfo(name = "country")
    val country: String,
    @ColumnInfo(name = "country_code")
    val countryCode: String,
    @ColumnInfo(name = "timezone")
    val timezone: String,
    @ColumnInfo(name = "last_update_time")
    val lastUpdateTime: Long = 0,
    @ColumnInfo(name = "is_gps_location")
    val isGpsLocation: Boolean = false
)

fun Place.canUpdate(intervalInMinutes: Int): Boolean =
    getTimeDiffInMinutes(lastUpdateTime) < intervalInMinutes

fun Place.newLastUpdateTime(): Long =
    getTimeInMilliseconds()

fun getTimeDiffInMinutes(time: Long): Long =
    ((getTimeInMilliseconds() - time) / 1000) / 60

private fun getTimeInMilliseconds(): Long =
    Calendar.getInstance().time.time