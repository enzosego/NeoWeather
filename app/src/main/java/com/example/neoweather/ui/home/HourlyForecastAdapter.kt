package com.example.neoweather.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.neoweather.databinding.ItemHourBinding
import com.example.neoweather.util.WeatherCodeMapping
import com.example.neoweather.data.model.hour.Hour
import com.example.neoweather.data.model.preferences.Preferences
import com.example.neoweather.util.WeatherUnits

class HourlyForecastAdapter : ListAdapter<Hour, ItemHourViewHolder>(
    DiffCallback
) {

    private val listSizeLimit = 24

    private var preferences: Preferences? = null

    private object DiffCallback : DiffUtil.ItemCallback<Hour>() {
        override fun areItemsTheSame(oldItem: Hour, newItem: Hour):
                Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Hour, newItem: Hour):
                Boolean = oldItem.temp == newItem.temp
                && oldItem.time == newItem.time

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHourViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ItemHourViewHolder(
            ItemHourBinding.inflate(layoutInflater, parent, false),
            preferences
        )
    }

    override fun onBindViewHolder(holder: ItemHourViewHolder, position: Int) {
        val hourData = getItem(position)
        holder.bind(hourData)
    }

    override fun getItemCount(): Int =
        if (currentList.isNotEmpty())
            listSizeLimit
        else
            0

    fun submitPreferences(newPreferences: Preferences?) {
        preferences = newPreferences
    }
}

class ItemHourViewHolder(private var binding: ItemHourBinding, private val preferences: Preferences?) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(hourData: Hour) {
        binding.time =
            WeatherUnits.getHourFromTime(hourData.time)

        binding.temperature =
            WeatherUnits.getTempUnit(
                hourData.temp,
                preferences?.isFahrenheitEnabled ?: true)

        binding.weatherCode =
            WeatherCodeMapping.description[hourData.weatherCode]

        binding.executePendingBindings()
    }
}
