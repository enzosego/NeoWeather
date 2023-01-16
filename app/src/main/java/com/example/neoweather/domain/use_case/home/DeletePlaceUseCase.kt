package com.example.neoweather.domain.use_case.home

import com.example.neoweather.data.repository.WeatherDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DeletePlaceUseCase(
    private val weatherDataRepository: WeatherDataRepository,
    private val coroutineScope: CoroutineScope
) {
    operator fun invoke(id: Int) = coroutineScope.launch {
        weatherDataRepository.deletePlace(id)
    }
}