package com.example.neoweather.domain.use_case.settings

import com.example.neoweather.domain.use_case.EnqueueWorkersUseCase
import com.example.neoweather.domain.use_case.UpdatePreferencesUseCase

data class SettingsUseCases(
    val enqueueWorkers: EnqueueWorkersUseCase,
    val updatePreferences: UpdatePreferencesUseCase
)