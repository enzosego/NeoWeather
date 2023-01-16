package com.example.neoweather.domain.use_case

import com.example.neoweather.data.repository.PreferencesRepository
import kotlin.math.roundToInt

class FormatTempUnitUseCase(private val preferencesRepository: PreferencesRepository) {

    operator fun invoke(temp: Double): String {
        val isFahrenheitEnabled =
            preferencesRepository.preferences.value?.isFahrenheitEnabled ?: false
        return getTempString(temp, isFahrenheitEnabled)
    }

    private fun getTempString(temp: Double, isFahrenheitEnabled: Boolean): String =
        if (isFahrenheitEnabled)
            "${celsiusToFahrenheit(temp)}°"
        else
            "${temp.roundToInt()}°"

    private fun celsiusToFahrenheit(celsiusTemp: Double): String =
        ((celsiusTemp * 9 / 5) + 32)
            .roundToInt()
            .toString()
}