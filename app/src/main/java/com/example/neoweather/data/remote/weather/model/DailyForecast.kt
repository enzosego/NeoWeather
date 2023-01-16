package com.example.neoweather.data.remote.weather.model

import com.example.neoweather.data.local.model.day.Day
import com.example.neoweather.data.local.model.day.DaysEntity
import com.example.neoweather.data.local.model.WeatherCodeMapping
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    val windSpeedMax: List<Double>
)

fun DailyForecast.asDatabaseModel(newId: Int): DaysEntity {
    val dayList = mutableListOf<Day>()

    for (i in time.indices) {
        val newDay = Day(
            time = getDayFromTime(time[i]),
            maxTemp = maxTemp[i],
            minTemp = minTemp[i],
            precipitationSum = precipitationSum.getOrNull(i) ?: 0.0,
            windDirectionDominant = windDirectionDominant.getOrNull(i) ?: 0.0,
            windSpeedMax = windSpeedMax.getOrNull(i) ?: 0.0,
            weatherDescription = WeatherCodeMapping.getDescription(weatherCode[i])
        )
        dayList.add(newDay)
    }
    return DaysEntity(
        id = newId,
        dayList = dayList
    )
}

private fun getDayFromTime(time: String): String =
    time.subSequence(5, 7).toString()