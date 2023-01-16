package com.example.neoweather.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.neoweather.data.local.NeoWeatherDatabase
import com.example.neoweather.data.local.model.preferences.Preferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PreferencesRepository(
    private val database: NeoWeatherDatabase,
    private val ioDispatcher: CoroutineDispatcher
) {

    val preferences: LiveData<Preferences> =
        database.preferencesDao.getPreferences().asLiveData()

    suspend fun updatePreferences(preferences: Preferences) {
        withContext(ioDispatcher) {
            database.preferencesDao.update(preferences)
        }
    }
}