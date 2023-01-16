package com.example.neoweather.data.remote.weather.model

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

internal fun getHour(time: String): Int =
    time.subSequence(11, 13).toString().toInt()

internal fun getDay(time: String): Int =
    time.subSequence(8, 10).toString().toInt()

@SuppressLint("SimpleDateFormat")
internal fun makeHourTimeInstance(time: String): Date =
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        .parse(time)!!