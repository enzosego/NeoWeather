package com.example.neoweather.data.model.current

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentWeatherDao {

    @Query("SELECT * FROM current_weather " +
            "WHERE id = :id")
    fun getEntity(id: Int): Flow<CurrentWeather>

    @Query("SELECT * FROM current_weather")
    fun getAllEntities(): Flow<List<CurrentWeather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currentWeather: CurrentWeather)
}