package com.example.neoweather.data.model.day

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DaysDao {

    @Query("SELECT * FROM days")
    fun getAllEntities(): Flow<List<DaysEntity>>

    @Query("UPDATE days SET id = :newId " +
            "WHERE :oldId = id")
    fun updateId(oldId: Int, newId: Int)

    @Query("DELETE FROM days " +
            "WHERE :id = id")
    fun delete(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(days: DaysEntity)
}