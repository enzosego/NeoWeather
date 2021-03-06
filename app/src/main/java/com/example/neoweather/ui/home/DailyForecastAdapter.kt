package com.example.neoweather.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.neoweather.databinding.ItemDayBinding
import com.example.neoweather.util.WeatherCodeMapping
import com.example.neoweather.data.model.day.Day
import com.example.neoweather.data.model.preferences.Preferences
import com.example.neoweather.util.WeatherUnits

class DailyForecastAdapter : ListAdapter<Day, ItemDayViewHolder>(
    DiffCallback
) {

    private var preferences: Preferences? = null

    private object DiffCallback : DiffUtil.ItemCallback<Day>() {
        override fun areItemsTheSame(oldItem: Day, newItem: Day):
                Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Day, newItem: Day):
                Boolean = oldItem.minTemp == newItem.minTemp
                && oldItem.maxTemp == newItem.maxTemp
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDayViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ItemDayViewHolder(
            ItemDayBinding.inflate(layoutInflater, parent, false),
            preferences
        )
    }

    override fun onBindViewHolder(holder: ItemDayViewHolder, position: Int) {
        val dayData = getItem(position)
        holder.bind(dayData)
    }

    fun submitPreferences(newPreferences: Preferences?) {
        preferences = newPreferences
    }
}

class ItemDayViewHolder(private var binding: ItemDayBinding, private val preferences: Preferences?) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(dayData: Day) {
        binding.time =
            WeatherUnits.getDayFromTime(dayData.time)

        binding.maxTemp =
            WeatherUnits.getTempUnit(
                dayData.maxTemp,
                preferences?.isFahrenheitEnabled ?: false)

        binding.weatherDescription =
            WeatherCodeMapping.description[dayData.weatherCode]

        binding.executePendingBindings()
    }
}