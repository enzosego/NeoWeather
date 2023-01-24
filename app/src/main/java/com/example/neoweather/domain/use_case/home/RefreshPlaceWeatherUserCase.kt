package com.example.neoweather.domain.use_case.home

import com.example.neoweather.data.repository.WeatherRepository

class RefreshPlaceWeatherUserCase(private val weatherRepository: WeatherRepository) {

    suspend operator fun invoke(id: Int) {
        weatherRepository.refreshWeatherAtLocation(id)
    }
}