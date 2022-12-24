package com.example.neoweather.remote.weather.model

import com.example.neoweather.data.model.hour.Hour
import com.example.neoweather.data.model.hour.HoursEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class HourlyForecast(
    val time: List<String>,
    @SerialName("temperature_2m")
    val temp: List<Double>,
    @SerialName("weathercode")
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