package com.example.neoweather.ui.days.day_detail.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.neoweather.R
import com.example.neoweather.data.local.model.hour.Hour
import com.example.neoweather.databinding.ItemHourDetailBinding
import com.example.neoweather.domain.use_case.FormatTempUnitUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class ItemHourDetailViewHolder(
    private var binding: ItemHourDetailBinding
) : RecyclerView.ViewHolder(binding.root), KoinComponent {

    private val formatTemp: FormatTempUnitUseCase by inject()

    fun bind(
        hourData: Hour,
        getSunriseTime: (hour: Hour) -> Date?,
        getSunsetTime: (hour: Hour) -> Date?
    ) {

        val calendar = Calendar.getInstance()

        val sunTiming: Date? = getSunriseTime(hourData) ?: getSunsetTime(hourData)

        if (sunTiming != null) {
            binding.sunTimingCard.visibility = View.VISIBLE

            calendar.time = sunTiming

            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val sunTimingTitle =
                if (hour < 12)
                    "Sunrise"
                else
                    "Sunset"

            binding.sunTimingTitle.text =
                itemView.context.getString(R.string.sun_timing, sunTimingTitle, hour, minute)
        } else
            binding.sunTimingCard.visibility = View.GONE

        calendar.time = hourData.time

        binding.time.text = calendar.get(Calendar.HOUR_OF_DAY).toString()

        binding.temp.text = formatTemp(hourData.temp)

        binding.weatherDescription.text = hourData.weatherDescription

        binding.executePendingBindings()
    }
}