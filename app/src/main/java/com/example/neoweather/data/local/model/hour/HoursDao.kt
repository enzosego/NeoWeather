package com.example.neoweather.data.local.model.hour

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HoursDao {

    @Query("SELECT * FROM hours")
    fun getAllEntities(): Flow<List<HoursEntity>>

    @Query("UPDATE hours SET id = :newId " +
            "WHERE :oldId = id")
    fun updateId(oldId: Int, newId: Int)

    @Query("DELETE FROM hours " +
            "WHERE :id = id")
    fun delete(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hours: HoursEntity)
}