package com.example.neoweather.data.model.preferences

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PreferencesDao {

    @Query("SELECT * FROM preferences")
    fun getPreferences(): Flow<Preferences>

    @Insert
    suspend fun insert(preferences: Preferences)

    @Update
    suspend fun update(preferences: Preferences)
}