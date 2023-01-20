package com.example.neoweather.ui.home.weather.adapter.daily

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.neoweather.data.local.model.day.Day
import com.example.neoweather.databinding.ItemDayBinding

class DailyForecastAdapter(
    private val clickListener: (dayPosition: Int) -> Unit
) : ListAdapter<Day, ItemDayViewHolder>(DiffCallback) {

    private object DiffCallback : DiffUtil.ItemCallback<Day>() {
        override fun areItemsTheSame(oldItem: Day, newItem: Day): Boolean =
            oldItem.time == newItem.time

        override fun areContentsTheSame(oldItem: Day, newItem: Day): Boolean =
            oldItem.minTemp == newItem.minTemp && oldItem.maxTemp == newItem.maxTemp
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDayViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ItemDayViewHolder(
            ItemDayBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemDayViewHolder, position: Int) {
        val dayData = getItem(position)
        holder.bind(dayData, clickListener)
    }
}