package com.example.neoweather.util

import kotlin.math.roundToInt

object WeatherUnits {
    fun getTempUnit(temp: Double, isFahrenheitEnabled: Boolean): String =
        if (isFahrenheitEnabled)
            "${celsiusToFahrenheit(temp)}°"
        else
            "${temp.roundToInt()}°"

    fun getHourFromTime(time: String): String =
        time.subSequence(11, 13).toString()

    fun getDayFromTime(time: String): String =
        time.takeLast(2)
}

fun celsiusToFahrenheit(celsiusTemp: Double): String =
    ((celsiusTemp * 9 / 5) + 32)
        .roundToInt()
        .toString()
