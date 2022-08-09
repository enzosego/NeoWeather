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

    fun formatPrecipitationSum(
        precipitation: Double,
        isInches: Boolean
    ): String =
        if (isInches)
            "${(precipitation / 25.4).roundToInt()}in"
        else
            "${precipitation.roundToInt()}mm"

    fun formatSpeedUnit(
        speed: Double,
        isMiles: Boolean
    ): String =
        if (isMiles)
            "${Math.round((speed / 1.609) * 10.0) / 10.0}mph"
        else
            "${speed}km/h"
}

fun celsiusToFahrenheit(celsiusTemp: Double): String =
    ((celsiusTemp * 9 / 5) + 32)
        .roundToInt()
        .toString()
