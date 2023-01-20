package com.example.neoweather.domain.use_case.day_detail

import com.example.neoweather.domain.use_case.FindCurrentHourIndexUseCase

data class DayDetailUseCases(
    val getDaysByHours: GetDaysByHoursUseCase,
    val findCurrentHourIndex: FindCurrentHourIndexUseCase
)