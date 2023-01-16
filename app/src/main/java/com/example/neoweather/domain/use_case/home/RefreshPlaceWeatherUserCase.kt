package com.example.neoweather.domain.use_case.home

import com.example.neoweather.data.repository.WeatherDataRepository

class RefreshPlaceWeatherUserCase(private val weatherDataRepository: WeatherDataRepository) {

    suspend operator fun invoke(id: Int) {
        weatherDataRepository.refreshPlaceWeather(id)
    }
}