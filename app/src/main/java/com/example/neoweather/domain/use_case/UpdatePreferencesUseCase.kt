package com.example.neoweather.domain.use_case

import com.example.neoweather.data.local.model.preferences.Preferences
import com.example.neoweather.data.repository.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UpdatePreferencesUseCase(
    private val preferencesRepository: PreferencesRepository,
    private val coroutineScope: CoroutineScope
) {
    operator fun invoke(newValue: Preferences) = coroutineScope.launch {
        preferencesRepository.updatePreferences(newValue)
    }
}