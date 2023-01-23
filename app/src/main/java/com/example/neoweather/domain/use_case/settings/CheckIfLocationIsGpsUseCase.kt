package com.example.neoweather.domain.use_case.settings

import com.example.neoweather.data.repository.PreferencesRepository
import com.example.neoweather.data.repository.WeatherDataRepository

class CheckIfLocationIsGpsUseCase(
    private val weatherDataRepository: WeatherDataRepository,
    private val preferencesRepository: PreferencesRepository
) {

    operator fun invoke(id: Int? = null): Boolean {
        val locationId =
            id ?: preferencesRepository.dataPreferences.value?.preferredLocationId ?: 0
        val location = weatherDataRepository.placesList.value!![locationId]

        return location.isGpsLocation
    }
}