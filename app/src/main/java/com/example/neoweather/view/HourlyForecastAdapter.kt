package com.example.neoweather.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.neoweather.databinding.ItemHourBinding
import com.example.neoweather.model.WeatherCodeMapping
import com.example.neoweather.viewmodel.HourData

class HourlyForecastAdapter : ListAdapter<HourData, HourlyForecastAdapter.ItemHourViewHolder>(DiffCallback) {

    class ItemHourViewHolder(private var binding: ItemHourBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hourData: HourData, weatherCodeMapping: WeatherCodeMapping) {
            binding.hourData = hourData
            binding.weatherCodeMapping = weatherCodeMapping
            binding.executePendingBindings()
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<HourData>() {
        override fun areItemsTheSame(oldItem: HourData, newItem: HourData):
                Boolean = oldItem.time == newItem.time

        override fun areContentsTheSame(oldItem: HourData, newItem: HourData):
                Boolean = oldItem.temp == newItem.temp

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHourViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ItemHourViewHolder(
            ItemHourBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemHourViewHolder, position: Int) {
        val hourData = getItem(position)
        holder.bind(hourData, WeatherCodeMapping)
    }
}