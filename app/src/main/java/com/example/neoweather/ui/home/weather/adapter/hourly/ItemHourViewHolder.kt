package com.example.neoweather.ui.home.weather.adapter.hourly

import androidx.recyclerview.widget.RecyclerView
import com.example.neoweather.data.local.model.hour.Hour
import com.example.neoweather.databinding.ItemHourBinding
import com.example.neoweather.domain.use_case.FormatTempUnitUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Calendar

class ItemHourViewHolder(
    private var binding: ItemHourBinding
) : RecyclerView.ViewHolder(binding.root), KoinComponent {

    private val formatTemp: FormatTempUnitUseCase by inject()

    fun bind(hourData: Hour) {

        val calendar = Calendar.getInstance()
        calendar.time = hourData.time

        binding.time.text = calendar.get(Calendar.HOUR_OF_DAY).toString()

        binding.temp.text = formatTemp(hourData.temp)

        binding.weatherDescription.text = hourData.weatherDescription

        binding.executePendingBindings()
    }
}