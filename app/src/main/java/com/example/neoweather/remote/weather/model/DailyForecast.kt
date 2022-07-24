package com.example.neoweather.remote.weather.model

import com.example.neoweather.data.model.day.Day
import com.example.neoweather.data.model.day.DaysEntity
import com.squareup.moshi.Json

data class DailyForecast(
    val time: List<String>,
    val sunrise: List<String>,
    val sunset: List<String>,
    @Json(name = "temperature_2m_max")
    val maxTemp: List<Double>,
    @Json(name = "temperature_2m_min")
    val minTemp: List<Double>,
    @Json(name = "precipitation_sum")
    val precipitationSum: List<Double>,
    @Json(name = "rain_sum")
    val rainSum: List<Double>,
    @Json(name = "winddirection_10m_dominant")
    val windDirectionDominant: List<Double>,
    @Json(name = "windspeed_10m_max")
    val windSpeedMax: List<Double>,
    @Json(name = "weathercode")
    val weatherCode: List<Int>
)

fun DailyForecast.asDatabaseModel(placeId: Int): DaysEntity {
    val dayList = mutableListOf<Day>()

    for (i in time.indices) {
        val newDay = Day(
            time = time[i],
            sunrise = sunrise[i],
            sunset = sunset[i],
            maxTemp = maxTemp[i],
            minTemp = minTemp[i],
            precipitationSum = precipitationSum.getOrNull(i) ?: 0.0,
            rainSum = rainSum.getOrNull(i) ?: 0.0,
            windDirectionDominant = windDirectionDominant.getOrNull(i) ?: 0.0,
            windSpeedMax = windSpeedMax.getOrNull(i) ?: 0.0,
            weatherCode = weatherCode[i]
        )
        dayList.add(newDay)
    }
    return DaysEntity(
        id = placeId,
        dayList = dayList
    )
}