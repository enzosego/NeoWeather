package com.example.neoweather.data.remote.weather.model

import android.annotation.SuppressLint
import com.example.neoweather.data.local.model.day.Day
import com.example.neoweather.data.local.model.day.DaysEntity
import com.example.neoweather.data.local.model.WeatherCodeMapping
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Serializable
data class DailyForecast(
    val time: List<String>,
    @SerialName("temperature_2m_max")
    val maxTemp: List<Double>,
    @SerialName("temperature_2m_min")
    val minTemp: List<Double>,
    @SerialName("precipitation_sum")
    val precipitationSum: List<Double>,
    @SerialName("weathercode")
    val weatherCode: List<Int>,
    @SerialName("winddirection_10m_dominant")
    val windDirectionDominant: List<Double>,
    @SerialName("windspeed_10m_max")
    val windSpeedMax: List<Double>,
    @SerialName("sunrise")
    val sunrise: List<String>,
    @SerialName("sunset")
    val sunset: List<String>
)

fun DailyForecast.asDatabaseModel(newId: Int): DaysEntity {
    val dayList = mutableListOf<Day>()

    for (i in time.indices) {
        val newDay = Day(
            time = makeDayTimeInstance(time[i]),
            maxTemp = maxTemp[i],
            minTemp = minTemp[i],
            precipitationSum = precipitationSum.getOrNull(i) ?: 0.0,
            windDirectionDominant = windDirectionDominant.getOrNull(i) ?: 0.0,
            windSpeedMax = windSpeedMax.getOrNull(i) ?: 0.0,
            weatherDescription = WeatherCodeMapping.getDescription(weatherCode[i]),
            sunrise = makeDateInstance(sunrise[i]),
            sunset = makeDateInstance(sunset[i])
        )
        dayList.add(newDay)
    }
    return DaysEntity(
        id = newId,
        dayList = dayList
    )
}

@SuppressLint("SimpleDateFormat")
private fun makeDayTimeInstance(time: String): Date =
    SimpleDateFormat("yyyy-MM-dd")
        .parse(time)!!