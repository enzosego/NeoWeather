package com.example.neoweather.ui.home.weather.adapter.hourly

import androidx.recyclerview.widget.RecyclerView
import com.example.neoweather.data.local.model.hour.Hour
import com.example.neoweather.databinding.ItemHourBinding
import com.example.neoweather.domain.use_case.FormatTempUnitUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ItemHourViewHolder(
    private var binding: ItemHourBinding
) : RecyclerView.ViewHolder(binding.root), KoinComponent {

    private val formatTemp: FormatTempUnitUseCase by inject()

    fun bind(hourData: Hour) {

        binding.time.text = hourData.time

        binding.temp.text = formatTemp(hourData.temp)

        binding.weatherDescription.text = hourData.weatherDescription

        binding.executePendingBindings()
    }
}