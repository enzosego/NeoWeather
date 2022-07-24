package com.example.neoweather.data.model.day

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DaysDao {

    @Query("SELECT * FROM days " +
            "WHERE id = :id")
    fun getEntity(id: Int): Flow<DaysEntity>

    @Query("SELECT * FROM days")
    fun getAllEntities(): Flow<List<DaysEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(days: DaysEntity)
}