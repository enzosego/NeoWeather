package com.example.neoweather.data.local.model.preferences.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DataPreferencesDao {

    @Query("SELECT * FROM data_preferences")
    fun getPreferences(): Flow<DataPreferences>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(unitsPreferences: DataPreferences)

    @Update
    suspend fun update(unitsPreferences: DataPreferences)
}