package com.example.neoweather.data.repository

import com.example.neoweather.data.local.NeoWeatherDatabase
import com.example.neoweather.data.local.model.preferences.data.DataPreferences
import com.example.neoweather.data.local.model.preferences.units.UnitsPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PreferencesRepository(
    private val database: NeoWeatherDatabase,
    private val ioDispatcher: CoroutineDispatcher
) {

    val unitsPreferencesFlow: Flow<UnitsPreferences> =
        database.unitsPreferencesDao.getPreferences()

    lateinit var unitsPreferences: UnitsPreferences

    val dataPreferencesFlow: Flow<DataPreferences> =
        database.dataPreferencesDao.getPreferences()

    lateinit var dataPreferences: DataPreferences

    init {
        collectPreferences()
    }

    suspend fun updateUnitPreferences(newValue: UnitsPreferences) =
        withContext(ioDispatcher) {
            database.unitsPreferencesDao.update(newValue)
        }

    suspend fun updateDataPreferences(newValue: DataPreferences) =
        withContext(ioDispatcher) {
            database.dataPreferencesDao.update(newValue)
        }

    private fun collectPreferences() {
        dataPreferencesFlow.collectValue(ioDispatcher) { dataPreferences = it }
        unitsPreferencesFlow.collectValue(ioDispatcher) { unitsPreferences = it }
    }
}