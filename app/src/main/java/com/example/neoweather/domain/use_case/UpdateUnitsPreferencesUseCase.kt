package com.example.neoweather.domain.use_case

import com.example.neoweather.data.local.model.preferences.units.UnitsPreferences
import com.example.neoweather.data.repository.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UpdateUnitsPreferencesUseCase(
    private val preferencesRepository: PreferencesRepository,
    private val coroutineScope: CoroutineScope
) {
    operator fun invoke(newValue: UnitsPreferences) = coroutineScope.launch {
        preferencesRepository.updateUnitPreferences(newValue)
    }
}