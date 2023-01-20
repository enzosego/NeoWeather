package com.example.neoweather.data.remote.weather.model

import android.annotation.SuppressLint
import com.example.neoweather.data.local.model.hour.Hour
import com.example.neoweather.data.local.model.hour.HoursEntity
import com.example.neoweather.data.local.model.WeatherCodeMapping
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Serializable
data class HourlyForecast(
    val time: List<String>,
    @SerialName("temperature_2m")
    val temp: List<Double>,
    @SerialName("weathercode")
    val weatherCode: List<Int>
)

fun HourlyForecast.asDatabaseModel(newId: Int): HoursEntity {
    val hourList = mutableListOf<Hour>()

    for (i in weatherCode.indices) {

        val newHour = Hour(
            time = makeHourTimeInstance(time[i]),
            temp = temp.getOrNull(i) ?: 0.0,
            WeatherCodeMapping.getDescription(weatherCode[i])
        )
        hourList.add(newHour)
    }
    return HoursEntity(
        id = newId,
        hourList = hourList
    )
}

@SuppressLint("SimpleDateFormat")
private fun makeHourTimeInstance(time: String): Date =
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        .parse(time)!!