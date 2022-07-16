package com.example.neoweather.data.model.place

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {

    @Query("SELECT * FROM place")
    fun getPlaceInfo(): Flow<Place>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: Place)
}