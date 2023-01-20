package com.example.neoweather.ui.home.weather.adapter.daily

import androidx.recyclerview.widget.RecyclerView
import com.example.neoweather.data.local.model.day.Day
import com.example.neoweather.databinding.ItemDayBinding
import com.example.neoweather.domain.use_case.FormatPrecipitationSumUseCase
import com.example.neoweather.domain.use_case.FormatSpeedUnitUseCase
import com.example.neoweather.domain.use_case.FormatTempUnitUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class ItemDayViewHolder(
    private var binding: ItemDayBinding
) : RecyclerView.ViewHolder(binding.root), KoinComponent {

    private val formatTemp: FormatTempUnitUseCase by inject()
    private val formatPrecipitation: FormatPrecipitationSumUseCase by inject()
    private val formatSpeed: FormatSpeedUnitUseCase by inject()

    fun bind(
        dayData: Day,
        clickListener: (dayPosition: Int) -> Unit
    ) {

        val calendar = Calendar.getInstance()
        calendar.time = dayData.time

        binding.time.text = calendar.get(Calendar.DAY_OF_MONTH).toString()

        binding.temp.text = formatTemp(dayData.maxTemp)

        binding.weatherDescription.text = dayData.weatherDescription

        binding.precipitation.text = formatPrecipitation(dayData.precipitationSum)

        binding.windSpeed.text = formatSpeed(dayData.windSpeedMax)

        binding.day = dayData

        binding.hourCard.setOnClickListener { clickListener(adapterPosition) }

        binding.executePendingBindings()
    }
}