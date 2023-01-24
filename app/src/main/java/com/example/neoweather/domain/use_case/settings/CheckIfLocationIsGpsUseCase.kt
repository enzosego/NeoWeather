package com.example.neoweather.domain.use_case.settings

import com.example.neoweather.data.repository.PlacesRepository
import com.example.neoweather.data.repository.PreferencesRepository

class CheckIfLocationIsGpsUseCase(
    private val placesRepository: PlacesRepository,
    private val preferencesRepository: PreferencesRepository
) {

    operator fun invoke(id: Int? = null): Boolean {
        val locationId = id ?: preferencesRepository.dataPreferences.preferredLocationId
        val location = placesRepository.placesList[locationId]

        return location.isGpsLocation
    }
}