package com.example.neoweather.domain.use_case

import com.example.neoweather.data.local.model.preferences.data.DataPreferences
import com.example.neoweather.data.repository.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UpdateDataPreferencesUseCase(
    private val preferencesRepository: PreferencesRepository,
    private val coroutineScope: CoroutineScope
) {

    operator fun invoke(newValue: DataPreferences) = coroutineScope.launch {
        preferencesRepository.updateDataPreferences(newValue)
    }
}