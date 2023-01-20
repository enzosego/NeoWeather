package com.example.neoweather.domain.use_case.day_detail

import com.example.neoweather.data.local.model.hour.Hour
import com.example.neoweather.data.repository.WeatherDataRepository
import com.example.neoweather.domain.model.DayByHours
import java.util.Calendar
import java.util.Date

class GetDaysByHoursUseCase(private val weatherDataRepository: WeatherDataRepository) {

    private val calendar = Calendar.getInstance()
    private val currentYear = calendar.get(Calendar.YEAR)
    private val currentMonth = calendar.get(Calendar.MONTH)
    private val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    operator fun invoke(placeId: Int): List<DayByHours> {
        val dayList = mutableListOf<DayByHours>()

        val hourList = weatherDataRepository.hourlyDataList.value?.get(placeId)!!.hourList
            .filter { (time) -> !isItPastDay(time) }

        val listOfHourLists = mutableListOf<MutableList<Hour>>()
        repeat(7) { listOfHourLists.add(mutableListOf()) }

        for (hour in hourList) {
            listOfHourLists.find { it.size < 24 }?.add(hour)
        }

        listOfHourLists.forEach { list ->
            if (list.isEmpty())
                return@forEach
            dayList.add(
                DayByHours(
                    dayOfWeek = getDayOfWeek(list[0].time),
                    dayOfMonth = getDayOfMonth(list[0].time),
                    monthNum = getMonthNum(list[0].time),
                    hourList = list
                )
            )
        }

        return dayList
    }

    private fun getDayOfWeek(time: Date): Int {
        calendar.time = time
        return calendar.get(Calendar.DAY_OF_WEEK)
    }

    private fun getDayOfMonth(time: Date): String {
        calendar.time = time
        return calendar.get(Calendar.DAY_OF_MONTH).toString()
    }

    private fun getMonthNum(hourTime: Date): Int {
        calendar.time = hourTime
        return calendar.get(Calendar.MONTH)
    }

    private fun isItPastDay(hourTime: Date): Boolean {
        calendar.time = hourTime
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return year < currentYear || month < currentMonth || day < currentDay
    }
}