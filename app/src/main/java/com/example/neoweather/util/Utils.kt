package com.example.neoweather.util

import java.time.Instant
import java.util.*

object Utils {
    const val TAG = "DEBUG"

    enum class NeoWeatherApiStatus { LOADING, DONE, ERROR }

    const val accessToken: String =
        "pk.fdd7a17016391ba2f9c3084f67679b44"

    fun getTimeDiffInMinutes(time: Long): Long =
        ((Date.from(Instant.now()).time - time) / 1000) / 60
}