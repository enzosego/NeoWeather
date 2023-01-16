package com.example.neoweather.domain.use_case.home

import com.example.neoweather.domain.use_case.*

data class HomeUseCases(
    val enqueueWorkers: EnqueueWorkersUseCase,
    val makeGeoLocationInstance: MakeGeoLocationInstanceUseCase,
    val refreshPlaceWeather: RefreshPlaceWeatherUserCase,
    val insertOrUpdatePlace: InsertOrUpdatePlaceUseCase,
    val deletePlace: DeletePlaceUseCase,
    val updatePreferences: UpdatePreferencesUseCase,
    val formatTempUnit: FormatTempUnitUseCase
)