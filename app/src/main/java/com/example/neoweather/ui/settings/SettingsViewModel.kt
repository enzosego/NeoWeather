package com.example.neoweather.ui.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.neoweather.data.model.preferences.Preferences
import com.example.neoweather.repository.NeoWeatherRepository
import com.example.neoweather.workers.GetCurrentLocationWorker
import com.example.neoweather.workers.NotificationUtils
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SettingsViewModel(
    private val repository: NeoWeatherRepository
) : ViewModel() {

    val preferences = repository.preferences

    val areNotificationsEnabled: LiveData<Boolean> = Transformations.map(preferences) { preferences ->
        preferences.areNotificationsEnabled
    }

    val currentInterval: LiveData<Long> = Transformations.map(preferences) { preferences ->
        preferences.notificationsInterval
    }

    private val _hasUserChangedInterval = MutableLiveData(false)
    val hasUserChangedInterval: LiveData<Boolean> = _hasUserChangedInterval

    fun updateTempUnit(selected: String) {
        val newValue = when(selected) {
            "Fahrenheit" -> true
            else -> false
        }
        val newPreferences = preferences.value!!.copy(
            isFahrenheitEnabled = newValue
        )
        updatePreferences(newPreferences)
    }

    fun updateSpeedUnit(selected: String) {
        val newValue = when (selected) {
            "Mph" -> true
            else -> false
        }
        val newPreferences = preferences.value!!.copy(
            isMilesEnabled = newValue
        )
        updatePreferences(newPreferences)
    }

    fun updateRainUnit(selected: String) {
        val newValue = when(selected) {
            "Inches" -> true
            else -> false
        }
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

    fun toggleNotifications(newValue: Boolean, context: Context) {
        val newPreferences = preferences.value!!.copy(
            areNotificationsEnabled = newValue
        )
        updatePreferences(newPreferences)

        when (newValue) {
            true -> startPeriodicWork(context)
            false -> cancelPeriodicWork(context)
        }
    }

    fun updateNotificationsInterval(selected: String) {
        val newValue = when(selected) {
            "One hour" -> 1L
            "Three hours" -> 3L
            "Six hours" -> 6L
            else -> 12L
        }
        val newPreferences = preferences.value!!.copy(
            notificationsInterval = newValue
        )
        updatePreferences(newPreferences)

        if (_hasUserChangedInterval.value == false)
            _hasUserChangedInterval.value = true
    }

    fun startPeriodicWork(context: Context) {
        val getCurrentLocationWorker =
            PeriodicWorkRequestBuilder<GetCurrentLocationWorker>(
                repeatInterval = currentInterval.value ?: 1L,
                TimeUnit.HOURS
            )
                .addTag(NotificationUtils.GET_CURRENT_LOCATION_WORK_TAG)
                .setInitialDelay(currentInterval.value ?: 1L, TimeUnit.HOURS)
                .build()

        WorkManager
            .getInstance(context)
            .enqueueUniquePeriodicWork(
                NotificationUtils.GET_CURRENT_LOCATION_WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                getCurrentLocationWorker
            )
    }

    private fun cancelPeriodicWork(context: Context) {
        WorkManager.getInstance(context)
            .cancelAllWorkByTag(NotificationUtils.GET_CURRENT_LOCATION_WORK_TAG)
    }
}