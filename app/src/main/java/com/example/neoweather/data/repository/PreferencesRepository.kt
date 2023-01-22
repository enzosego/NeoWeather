package com.example.neoweather.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.neoweather.data.local.NeoWeatherDatabase
import com.example.neoweather.data.local.model.preferences.data.DataPreferences
import com.example.neoweather.data.local.model.preferences.units.UnitsPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PreferencesRepository(
    private val database: NeoWeatherDatabase,
    private val ioDispatcher: CoroutineDispatcher
) {

    val unitsPreferences: LiveData<UnitsPreferences> =
        database.unitsPreferencesDao.getPreferences().asLiveData()

    val dataPreferences: LiveData<DataPreferences> =
        database.dataPreferencesDao.getPreferences().asLiveData()

    suspend fun updateUnitPreferences(newValue: UnitsPreferences) {
        withContext(ioDispatcher) {
            database.unitsPreferencesDao.update(newValue)
        }
    }

    suspend fun updateDataPreferences(newValue: DataPreferences) {
        withContext(ioDispatcher) {
            database.dataPreferencesDao.update(newValue)
        }
    }
}