package com.example.neoweather.data.local.model.preferences.units

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UnitsPreferencesDao {

    @Query("SELECT * FROM units_preferences")
    fun getPreferences(): Flow<UnitsPreferences>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(unitsPreferences: UnitsPreferences)

    @Update
    suspend fun update(unitsPreferences: UnitsPreferences)
}