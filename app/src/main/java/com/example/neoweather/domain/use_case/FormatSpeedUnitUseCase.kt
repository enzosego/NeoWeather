package com.example.neoweather.domain.use_case

import com.example.neoweather.data.repository.PreferencesRepository
import kotlin.math.roundToInt

class FormatSpeedUnitUseCase(private val preferencesRepository: PreferencesRepository) {

    operator fun invoke(speed: Double): String =
        formatSpeedUnit(
            speed,
            isMiles = preferencesRepository.unitsPreferences.isMilesEnabled
        )

    private fun formatSpeedUnit(speed: Double, isMiles: Boolean): String =
        if (isMiles)
            "${((speed / 1.609) * 10.0).roundToInt() / 10.0}mph"
        else
            "${speed}km/h"
}