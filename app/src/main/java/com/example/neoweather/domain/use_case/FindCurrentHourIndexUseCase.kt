package com.example.neoweather.domain.use_case

import com.example.neoweather.data.local.model.hour.Hour
import java.util.*

class FindCurrentHourIndexUseCase {

    private val calendar = Calendar.getInstance()
    private val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    private val currentDay  = calendar.get(Calendar.DAY_OF_MONTH)

    operator fun invoke(hourList: List<Hour>?): Int {
        if (hourList.isNullOrEmpty())
            return -1
        var currentHourIndex = 0
        for ((i, hour) in hourList.withIndex()) {
            if (isItPastHour(hour.time))
                continue
            currentHourIndex = i
            break
        }
        return currentHourIndex
    }

    private fun isItPastHour(time: Date): Boolean {
        calendar.time = time
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return hour < currentHour || day < currentDay
    }
}