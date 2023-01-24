package com.example.neoweather.data.repository

import com.example.neoweather.data.local.NeoWeatherDatabase
import com.example.neoweather.data.local.model.place.Place
import com.example.neoweather.data.remote.geocoding.GeoCodingApi
import com.example.neoweather.data.remote.geocoding.model.GeoLocation
import com.example.neoweather.data.remote.geocoding.model.asDatabaseModel
import com.example.neoweather.data.remote.geocoding.model.isGpsLocation
import com.example.neoweather.data.remote.reverse_geocoding.ReverseGeoCodingApi
import com.example.neoweather.data.remote.reverse_geocoding.model.getName
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PlacesRepository(
    private val database: NeoWeatherDatabase,
    private val geoCodingSource: GeoCodingApi,
    private val reverseGeoCodingSource: ReverseGeoCodingApi,
    private val ioDispatcher: CoroutineDispatcher
) {

    val placesFlow: Flow<List<Place>> =
        database.placeDao.getAllPlaces()

    var placesList: List<Place> = emptyList()

    init {
        collectPlaces()
    }

    suspend fun updateOrInsertPlace(location: GeoLocation) =
        if (location.isGpsLocation())
            getPlaceFromGps(location)
        else
            insertNewPlace(location)

    private suspend fun getPlaceFromGps(location: GeoLocation) =
        withContext(ioDispatcher) {
            val places = database.placeDao.matchGpsLocation()

            if (places.isEmpty()) {
                increaseIds()
                insertNewPlace(location, id = 0)
            }
            else
                savePlaceInDatabase(
                    places.first().copy(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                )
        }

    private suspend fun insertNewPlace(location: GeoLocation, id: Int? = null) =
        withContext(ioDispatcher) {
            val placeName = location.name.ifEmpty {
                reverseGeoCodingSource
                    .getLocationName(
                        location.latitude,
                        location.longitude)
                    .address
                    .getName()
            }
            val place = geoCodingSource.getLocation(placeName)
                .results[0]
                .asDatabaseModel(id ?: getNewPlaceId(), location.isGpsLocation())

            savePlaceInDatabase(place)
        }

    suspend fun savePlaceInDatabase(place: Place) =
        withContext(ioDispatcher) {
            database.placeDao.insert(place)
        }

    suspend fun deletePlace(placeId: Int) =
        withContext(ioDispatcher) {
            database.placeDao.delete(placeId)
            database.hoursDao.delete(placeId)
            database.daysDao.delete(placeId)
            database.currentWeatherDao.delete(placeId)

            decreaseIds(placeId + 1)
        }

    private suspend fun decreaseIds(placeId: Int) =
        withContext(ioDispatcher) {
            for (i in placeId..placesList.size) {
                database.placeDao.updateId(i, i-1)
                database.hoursDao.updateId(i, i-1)
                database.daysDao.updateId(i, i-1)
                database.currentWeatherDao.updateId(i, i-1)
            }
        }

    private suspend fun increaseIds() =
        withContext(ioDispatcher) {
            for (i in placesList.size downTo 0) {
                database.placeDao.updateId(i, i+1)
                database.hoursDao.updateId(i, i+1)
                database.daysDao.updateId(i, i+1)
                database.currentWeatherDao.updateId(i, i+1)
            }
        }

    private fun getNewPlaceId(): Int =
        placesList.size

    private fun collectPlaces() =
        placesFlow.collectValue(ioDispatcher) { placesList = it }
}