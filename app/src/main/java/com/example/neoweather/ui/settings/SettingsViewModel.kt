package com.example.neoweather.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neoweather.data.model.preferences.Preferences
import com.example.neoweather.repository.NeoWeatherRepository
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: NeoWeatherRepository
) : ViewModel() {

    val preferences = repository.preferences

    fun updateTempUnit(newValue: Boolean) {
        val newPreferences = preferences.value!!.copy(
            isFahrenheitEnabled = newValue
        )
        updatePreferences(newPreferences)
    }

    fun updateSpeedUnit(newValue: Boolean) {
        val newPreferences = preferences.value!!.copy(
            isMilesEnabled = newValue
        )
        updatePreferences(newPreferences)
    }

    fun updateRainUnit(newValue: Boolean) {
        val newPreferences = preferences.value!!.copy(
            isInchesEnabled = newValue
        )
        updatePreferences(newPreferences)
    }

    private fun updatePreferences(newValue: Preferences) {
        viewModelScope.launch {
            repository.updatePreferences(newValue)
        }
    }
}