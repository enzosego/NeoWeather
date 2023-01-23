package com.example.neoweather.domain.use_case.home

import com.example.neoweather.domain.use_case.*
import com.example.neoweather.domain.use_case.settings.CheckIfLocationIsGpsUseCase

data class HomeUseCases(
    val makeGeoLocationInstance: MakeGeoLocationInstanceUseCase,
    val refreshPlaceWeather: RefreshPlaceWeatherUserCase,
    val insertOrUpdatePlace: InsertOrUpdatePlaceUseCase,
    val deletePlace: DeletePlaceUseCase,
    val updateUnitsPreferences: UpdateUnitsPreferencesUseCase,
    val updateDataPreferences: UpdateDataPreferencesUseCase,
    val formatTempUnit: FormatTempUnitUseCase,
    val scheduleQueueHandler: ScheduleQueueHandlerUseCase,
    val checkIfLocationIsGps: CheckIfLocationIsGpsUseCase
)