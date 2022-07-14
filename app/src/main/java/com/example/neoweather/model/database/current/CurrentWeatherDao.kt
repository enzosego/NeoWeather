package com.example.neoweather.model.database.current

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentWeatherDao {

    @Query("SELECT * FROM current_data")
    fun getCurrentWeather(): Flow<CurrentWeather>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currentWeather: CurrentWeather)
}