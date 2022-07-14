package com.example.neoweather.model.remote.weather

import com.example.neoweather.model.database.hour.Hour
import com.squareup.moshi.Json
import java.util.*

data class HourlyForecast(
    val time: List<String>,
    @Json(name = "temperature_2m")
    val temp: List<Double>,
    @Json(name = "weathercode")
    val weatherCode: List<Int>
)

fun HourlyForecast.asDatabaseModel(): List<Hour> {
    val hourList = mutableListOf<Hour>()
    val currentHour = Calendar.getInstance()
        .get(Calendar.HOUR_OF_DAY)
    var areOldHoursSkipped = false

    for (i in weatherCode.indices) {
        if (getHour(time[i]) < currentHour && !areOldHoursSkipped)
            continue
        else if (getHour(time[i]) == currentHour)
            areOldHoursSkipped = true

        val newHour = Hour(
            id = i,
            time = time[i].subSequence(11, 16).toString(),
            temp = temp[i].toString(),
            weatherCode = weatherCode[i]
        )
        hourList.add(newHour)
    }
    return hourList
}

private fun getHour(time: String): Int =
    time.subSequence(11, 13).toString().toInt()