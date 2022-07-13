package com.example.neoweather.model.database.day

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DayDao {

    @Query("SELECT * FROM day")
    fun getAllDays(): Flow<List<Day>>

    @Insert
    suspend fun insert(day: Day)

    @Update
    suspend fun update(day: Day)
}