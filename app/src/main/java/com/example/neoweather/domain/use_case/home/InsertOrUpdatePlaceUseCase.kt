package com.example.neoweather.domain.use_case.home

import com.example.neoweather.data.remote.geocoding.model.GeoLocation
import com.example.neoweather.data.repository.WeatherDataRepository

class InsertOrUpdatePlaceUseCase(private val weatherDataRepository: WeatherDataRepository) {

    suspend operator fun invoke(location: GeoLocation) {
        weatherDataRepository.updateOrInsertPlace(location)
    }
}