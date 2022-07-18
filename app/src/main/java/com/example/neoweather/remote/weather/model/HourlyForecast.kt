package com.example.neoweather.remote.weather.model

import android.util.Log
import androidx.databinding.adapters.TimePickerBindingAdapter.getHour
import com.example.neoweather.data.model.hour.Hour
import com.squareup.moshi.Json
import java.util.*

data class HourlyForecast(
    val time: List<String>,
    @Json(name = "temperature_2m")
    val temp: List<Double>,
    @Json(name = "weathercode")
    val weatherCode: List<Int>
)

/*
TODO: Fix timezone bug -
 If you select a location from different timezone than your phone the times for
 the hourly data will be wrong
 */
fun HourlyForecast.asDatabaseModel(): List<Hour> {
    val hourList = mutableListOf<Hour>()
    val currentHour = Calendar.getInstance()
        .get(Calendar.HOUR_OF_DAY)
    val currentDay  = Calendar.getInstance()
        .get(Calendar.DAY_OF_MONTH)
    var startIndex: Int? = null

    for (i in weatherCode.indices) {
        if ((getHour(time[i]) < currentHour || getDay(time[i]) < currentDay)
            && startIndex == null)
            continue
        else if (startIndex == null)
            startIndex = i

        val newHour = Hour(
            id = i - startIndex,
            time = time[i],
            temp = temp[i],
            weatherCode = weatherCode[i]
        )
        hourList.add(newHour)
    }
    return hourList
}

private fun getHour(time: String): Int =
    time.subSequence(11, 13).toString().toInt()

private fun getDay(time: String): Int =
    time.subSequence(8, 10).toString().toInt()