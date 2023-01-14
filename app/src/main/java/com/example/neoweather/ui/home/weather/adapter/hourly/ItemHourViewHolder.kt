package com.example.neoweather.ui.home.weather.adapter.hourly

import androidx.recyclerview.widget.RecyclerView
import com.example.neoweather.data.local.model.hour.Hour
import com.example.neoweather.data.local.model.preferences.Preferences
import com.example.neoweather.databinding.ItemHourBinding
import com.example.neoweather.ui.utils.WeatherUnits
import com.example.neoweather.ui.utils.WeatherCodeMapping

class ItemHourViewHolder(
    private var binding: ItemHourBinding,
    private val preferences: Preferences?
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(hourData: Hour) {
        binding.time =
            WeatherUnits.getHourFromTime(hourData.time)

        binding.temperature =
            WeatherUnits.getTempUnit(
                hourData.temp,
                isFahrenheitEnabled = preferences?.isFahrenheitEnabled ?: false
            )

        binding.weatherCode =
            WeatherCodeMapping.description[hourData.weatherCode]

        binding.executePendingBindings()
    }
}