package com.example.neoweather.domain.use_case.settings

import com.example.neoweather.domain.use_case.ScheduleQueueHandlerUseCase
import com.example.neoweather.domain.use_case.UpdateDataPreferencesUseCase
import com.example.neoweather.domain.use_case.UpdateUnitsPreferencesUseCase

data class SettingsUseCases(
    val updateUnitsPreferences: UpdateUnitsPreferencesUseCase,
    val updateDataPreferences: UpdateDataPreferencesUseCase,
    val scheduleQueueHandler: ScheduleQueueHandlerUseCase,
    val checkIfLocationIsGps: CheckIfLocationIsGpsUseCase
)