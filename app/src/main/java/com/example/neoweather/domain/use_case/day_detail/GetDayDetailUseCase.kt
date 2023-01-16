package com.example.neoweather.domain.use_case.day_detail

import com.example.neoweather.data.repository.WeatherDataRepository
import com.example.neoweather.domain.model.DayByHours
import java.util.Calendar
import java.util.Date

class GetDayDetailUseCase(private val weatherDataRepository: WeatherDataRepository) {

    operator fun invoke(dayNum: Int, position: Int): DayByHours =
        DayByHours(
            dayOfWeek = "",
            dayNum = "23",
            hourList = weatherDataRepository.hourlyDataList.value?.get(position)!!.hourList
                .filter { isItCorrectDay(it.time, dayNum) }
        )

    private fun isItCorrectDay(time: Date, dayNum: Int): Boolean {
        val calendar = Calendar.getInstance()
        calendar.time = time
        return calendar.get(Calendar.DAY_OF_MONTH) == dayNum
    }
}