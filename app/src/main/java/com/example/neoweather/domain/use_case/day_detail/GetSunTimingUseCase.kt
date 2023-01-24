package com.example.neoweather.domain.use_case.day_detail

import com.example.neoweather.data.local.model.day.Day
import com.example.neoweather.data.local.model.hour.Hour
import com.example.neoweather.data.repository.WeatherRepository
import java.util.*

class GetSunTimingUseCase(private val weatherRepository: WeatherRepository) {

    operator fun invoke(
        hour: Hour,
        placeId: Int,
        getTimingKey: (day: Day) -> Date
    ): Date? {

        val matchingDay =
            getMatchingDay(weatherRepository.dailyDataList[placeId].dayList, hour)

        val sunTiming = getTimingKey(matchingDay)

        val datesDiff = datesDiffInMinutes(sunTiming, hour.time)
        return if (datesDiff in -59..0)
            sunTiming
        else
            null
    }
}