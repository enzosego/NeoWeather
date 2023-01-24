package com.example.neoweather.domain.use_case.home

import com.example.neoweather.data.repository.PlacesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DeletePlaceUseCase(
    private val placesRepository: PlacesRepository,
    private val coroutineScope: CoroutineScope
) {
    operator fun invoke(id: Int) = coroutineScope.launch {
        placesRepository.deletePlace(id)
    }
}