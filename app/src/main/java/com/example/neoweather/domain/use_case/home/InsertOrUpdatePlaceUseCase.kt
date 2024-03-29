package com.example.neoweather.domain.use_case.home

import com.example.neoweather.data.remote.geocoding.model.GeoLocation
import com.example.neoweather.data.repository.PlacesRepository

class InsertOrUpdatePlaceUseCase(private val placesRespository: PlacesRepository) {

    suspend operator fun invoke(location: GeoLocation) {
        placesRespository.updateOrInsertPlace(location)
    }
}