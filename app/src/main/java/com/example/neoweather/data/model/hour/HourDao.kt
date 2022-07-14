package com.example.neoweather.data.model.hour

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HourDao {

    @Query("SELECT * FROM hour")
    fun getAllHours(): Flow<List<Hour>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<Hour>)
}