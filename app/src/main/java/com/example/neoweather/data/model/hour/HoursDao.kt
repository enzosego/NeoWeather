package com.example.neoweather.data.model.hour

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HoursDao {

    @Query("SELECT * FROM hours " +
            "WHERE id = :id")
    fun getEntity(id: Int): Flow<HoursEntity>

    @Query("SELECT * FROM hours")
    fun getAllEntities(): Flow<List<HoursEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hours: HoursEntity)
}