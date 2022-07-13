package com.example.neoweather.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.neoweather.databinding.ItemDayBinding
import com.example.neoweather.model.WeatherCodeMapping
import com.example.neoweather.model.database.day.Day

class DailyForecastAdapter : ListAdapter<Day, ItemDayViewHolder>(
    DiffCallback
) {

    private object DiffCallback : DiffUtil.ItemCallback<Day>() {
        override fun areItemsTheSame(oldItem: Day, newItem: Day):
                Boolean = oldItem.time == newItem.time

        override fun areContentsTheSame(oldItem: Day, newItem: Day):
                Boolean = oldItem.minTemp == newItem.minTemp
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemDayViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ItemDayViewHolder(
            ItemDayBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemDayViewHolder, position: Int) {
        val dayData = getItem(position)
        holder.bind(dayData, WeatherCodeMapping)
    }
}

class ItemDayViewHolder(private var binding: ItemDayBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(dayData: Day, weatherCodeMapping: WeatherCodeMapping) {
        binding.dayData = dayData
        binding.weatherCodeMapping = weatherCodeMapping
        binding.executePendingBindings()
    }
}
