package com.example.neoweather.ui.settings

import androidx.lifecycle.*
import androidx.work.ExistingPeriodicWorkPolicy
import com.example.neoweather.data.repository.PreferencesRepository
import com.example.neoweather.domain.use_case.settings.SettingsUseCases

class SettingsViewModel(
    preferencesRepository: PreferencesRepository,
    private val settingsUseCases: SettingsUseCases
) : ViewModel() {

    val unitsPreferences = preferencesRepository.unitsPreferencesFlow.asLiveData()

    val dataPreferences = preferencesRepository.dataPreferencesFlow.asLiveData()

    val areNotificationsEnabled: LiveData<Boolean> =
        Transformations.map(dataPreferences) { it.areNotificationsEnabled }

    val updateInBackground: LiveData<Boolean> =
        Transformations.map(dataPreferences) { it.updateInBackground }

    fun updateTempUnit(selected: String) {
        val newValue = when(selected) {
            "Fahrenheit" -> true
            else -> false
        }
        val newPreferences = unitsPreferences.value!!.copy(
            isFahrenheitEnabled = newValue
        )
        settingsUseCases.updateUnitsPreferences(newPreferences)
    }

    fun updateSpeedUnit(selected: String) {
        val newValue = when (selected) {
            "Mph" -> true
            else -> false
        }
        val newPreferences = unitsPreferences.value!!.copy(
            isMilesEnabled = newValue
        )
        settingsUseCases.updateUnitsPreferences(newPreferences)
    }

    fun updateRainUnit(selected: String) {
        val newValue = when(selected) {
            "Inches" -> true
            else -> false
        }
        val newPreferences = unitsPreferences.value!!.copy(
            isInchesEnabled = newValue
        )
        settingsUseCases.updateUnitsPreferences(newPreferences)
    }

    fun toggleNotifications(newValue: Boolean) {
        val newPreferences = dataPreferences.value!!.copy(
            areNotificationsEnabled = newValue
        )
        settingsUseCases.updateDataPreferences(newPreferences)
    }

    fun toggleBackgroundUpdates(newValue: Boolean) {
        val newPreferences = dataPreferences.value!!.copy(
            updateInBackground = newValue
        )
        settingsUseCases.updateDataPreferences(newPreferences)
        updateQueueHandler()
    }

    fun setDatabaseUpdateInterval(selected: String) {
        val newValue = when(selected) {
            "One hour" -> 1L
            "Three hours" -> 3L
            "Six hours" -> 6L
            else -> 12L
        }
        val newPreferences = dataPreferences.value!!.copy(
            updateInterval = newValue
        )
        settingsUseCases.updateDataPreferences(newPreferences)
        updateQueueHandler()
    }

    private fun updateQueueHandler() {
        settingsUseCases.scheduleQueueHandler(ExistingPeriodicWorkPolicy.UPDATE)
    }

    fun isPreferredLocationGps(): Boolean =
        settingsUseCases.checkIfLocationIsGps()
}