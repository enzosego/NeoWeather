package com.example.neoweather.ui.home.weather.adapter.hourly

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.neoweather.data.model.hour.Hour
import com.example.neoweather.data.model.preferences.Preferences
import com.example.neoweather.databinding.ItemHourBinding

class HourlyForecastAdapter : ListAdapter<Hour, ItemHourViewHolder>(
    DiffCallback
) {

    private val listSizeLimit = 24

    private var preferences: Preferences? = null

    fun submitPreferences(newPreferences: Preferences?) {
        preferences = newPreferences
    }

    private object DiffCallback : DiffUtil.ItemCallback<Hour>() {
        override fun areItemsTheSame(oldItem: Hour, newItem: Hour):
                Boolean = oldItem.time == newItem.time

        override fun areContentsTheSame(oldItem: Hour, newItem: Hour):
                Boolean = oldItem.temp == newItem.temp
                && oldItem.weatherCode == newItem.weatherCode

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
}