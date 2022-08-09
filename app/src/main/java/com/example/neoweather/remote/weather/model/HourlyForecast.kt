package com.example.neoweather.remote.weather.model

import com.example.neoweather.data.model.hour.Hour
import com.example.neoweather.data.model.hour.HoursEntity
import com.squareup.moshi.Json
import java.util.*

data class HourlyForecast(
    val time: List<String>,
    @Json(name = "temperature_2m")
    val temp: List<Double>,
    @Json(name = "weathercode")
    val weatherCode: List<Int>
)

fun HourlyForecast.asDatabaseModel(newId: Int, timezone: String): HoursEntity {
    val hourList = mutableListOf<Hour>()
    val currentHour = Calendar.getInstance(TimeZone.getTimeZone(timezone))
        .get(Calendar.HOUR_OF_DAY)
    val currentDay  = Calendar.getInstance()
        .get(Calendar.DAY_OF_MONTH)
    var arePastHoursDiscarded = false

    for (i in weatherCode.indices) {
        if ((getHour(time[i]) < currentHour || getDay(time[i]) < currentDay)
            && !arePastHoursDiscarded)
            continue
        else if (!arePastHoursDiscarded)
            arePastHoursDiscarded = true

        val newHour = Hour(
            time = time[i],
            temp = temp.getOrNull(i) ?: 0.0,
            weatherCode = weatherCode.getOrNull(i) ?: 0
        )
        hourList.add(newHour)
    }
    return HoursEntity(
        id = newId,
        hourList = hourList
    )
}

private fun getHour(time: String): Int =
    time.subSequence(11, 13).toString().toInt()

private fun getDay(time: String): Int =
    time.subSequence(8, 10).toString().toInt()