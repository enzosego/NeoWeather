package com.example.neoweather.domain.use_case.day_detail

import com.example.neoweather.data.local.model.day.Day
import com.example.neoweather.data.local.model.hour.Hour
import com.example.neoweather.data.repository.WeatherDataRepository
import java.util.*

class GetSunTimingUseCase(private val weatherDataRepository: WeatherDataRepository) {

    operator fun invoke(
        hour: Hour,
        placeId: Int,
        getTimingKey: (day: Day) -> Date
    ): Date? {

        val matchingDay =
            getMatchingDay(weatherDataRepository.dailyDataList.value!![placeId].dayList, hour)

        val sunTiming = getTimingKey(matchingDay)

        val datesDiff = datesDiffInMinutes(sunTiming, hour.time)
        return if (datesDiff in -59..0)
            sunTiming
        else
            null
    }
}