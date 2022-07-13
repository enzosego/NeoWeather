package com.example.neoweather.model.database.hour

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HourDao {

    @Query("SELECT * FROM hour")
    fun getAllHours(): Flow<List<Hour>>

    @Insert
    suspend fun insert(hour: Hour)

    @Update
    suspend fun update(hour: Hour)
}