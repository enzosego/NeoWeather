package com.example.neoweather.data.local.model.preferences

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PreferencesDao {

    @Query("SELECT * FROM preferences")
    fun getPreferences(): Flow<Preferences>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(preferences: Preferences)

    @Update
    suspend fun update(preferences: Preferences)
}