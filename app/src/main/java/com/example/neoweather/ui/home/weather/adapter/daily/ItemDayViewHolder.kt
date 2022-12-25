package com.example.neoweather.ui.home.weather.adapter.daily

import androidx.recyclerview.widget.RecyclerView
import com.example.neoweather.data.model.day.Day
import com.example.neoweather.data.model.preferences.Preferences
import com.example.neoweather.databinding.ItemDayBinding
import com.example.neoweather.ui.utils.WeatherUnits
import com.example.neoweather.ui.utils.WeatherCodeMapping

class ItemDayViewHolder(
    private var binding: ItemDayBinding,
    private val preferences: Preferences?
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(dayData: Day) {
        binding.time =
            WeatherUnits.getDayFromTime(dayData.time)

        binding.maxTemp =
            WeatherUnits.getTempUnit(
                dayData.maxTemp,
                isFahrenheitEnabled = preferences?.isFahrenheitEnabled ?: false
            )

        binding.weatherDescription =
            WeatherCodeMapping.description[dayData.weatherCode]

        binding.precipitation =
            WeatherUnits.formatPrecipitationSum(
                dayData.precipitationSum,
                preferences?.isInchesEnabled ?: false
            )

        binding.windSpeed =
            WeatherUnits.formatSpeedUnit(
                dayData.windSpeedMax,
                preferences?.isMilesEnabled ?: false
            )

        binding.executePendingBindings()
    }
}