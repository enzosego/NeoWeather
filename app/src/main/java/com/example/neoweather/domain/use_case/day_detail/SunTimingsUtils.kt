package com.example.neoweather.domain.use_case.day_detail

import com.example.neoweather.data.local.model.day.Day
import com.example.neoweather.data.local.model.hour.Hour
import java.util.*

internal fun getMatchingDay(dayList: List<Day>, hour: Hour): Day =
    dayList.find { getDayOfTheMonth(it.time) == getDayOfTheMonth(hour.time) }!!

private fun getDayOfTheMonth(time: Date): Int {
    val calendar = Calendar.getInstance()
    calendar.time = time
    return calendar.get(Calendar.DAY_OF_MONTH)
}

internal fun datesDiffInMinutes(date1: Date, date2: Date): Long =
    ((date1.time - date2.time) / 1000) / 60