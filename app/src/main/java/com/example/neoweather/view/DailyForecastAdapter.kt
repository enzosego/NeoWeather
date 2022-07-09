package com.example.neoweather.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.neoweather.databinding.ItemDayBinding
import com.example.neoweather.model.WeatherCodeMapping
import com.example.neoweather.viewmodel.DayData
import com.example.neoweather.viewmodel.HourData

class DailyForecastAdapter : ListAdapter<DayData, DailyForecastAdapter.ItemDayViewHolder>(DiffCallback) {

    private object DiffCallback : DiffUtil.ItemCallback<DayData>() {
        override fun areItemsTheSame(oldItem: DayData, newItem: DayData):
                Boolean = oldItem.time == newItem.time

        override fun areContentsTheSame(oldItem: DayData, newItem: DayData):
                Boolean = oldItem.minTemp == newItem.minTemp
    }

    class ItemDayViewHolder(private var binding: ItemDayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dayData: DayData, weatherCodeMapping: WeatherCodeMapping) {
            binding.dayData = dayData
            binding.weatherCodeMapping = weatherCodeMapping
            binding.executePendingBindings()
        }
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