package com.example.neoweather.data.model.place

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {

    @Query("SELECT * FROM place")
    fun getAllPlaces(): Flow<List<Place>>

    @Query("SELECT * FROM place " +
            "WHERE :placeName = name " +
            "AND :countryName = country")
    fun getMatchingPlace(placeName: String, countryName: String): List<Place>

    @Query("UPDATE place SET id = :newId " +
            "WHERE :oldId = id")
    fun updateId(oldId: Int, newId: Int)

    @Query("DELETE FROM place " +
            "WHERE :id = id")
    fun delete(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: Place)
}