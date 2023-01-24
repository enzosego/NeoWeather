package com.example.neoweather.domain.use_case

import com.example.neoweather.data.repository.PreferencesRepository
import kotlin.math.roundToInt

class FormatTempUnitUseCase(private val preferencesRepository: PreferencesRepository) {

    operator fun invoke(temp: Double): String =
        if (preferencesRepository.unitsPreferences.isFahrenheitEnabled)
            "${celsiusToFahrenheit(temp)}°"
        else
            "${temp.roundToInt()}°"

    private fun celsiusToFahrenheit(celsiusTemp: Double): String =
        ((celsiusTemp * 9 / 5) + 32)
            .roundToInt()
            .toString()
}