package com.example.neoweather.domain.use_case

import com.example.neoweather.data.repository.PreferencesRepository
import kotlin.math.roundToInt

class FormatPrecipitationSumUseCase(private val preferencesRepository: PreferencesRepository) {

    operator fun invoke(sum: Double): String =
        formatPrecipitationSum(
            sum ,
            isInches = preferencesRepository.unitsPreferences.isInchesEnabled
        )

    private fun formatPrecipitationSum(precipitation: Double, isInches: Boolean): String =
        if (isInches)
            "${(precipitation / 25.4).roundToInt()}in"
        else
            "${precipitation.roundToInt()}mm"
}