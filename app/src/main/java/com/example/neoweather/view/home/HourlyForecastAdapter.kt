package com.example.neoweather.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.neoweather.databinding.ItemHourBinding
import com.example.neoweather.model.WeatherCodeMapping
import com.example.neoweather.model.database.hour.Hour

class HourlyForecastAdapter : ListAdapter<Hour, ItemHourViewHolder>(
    DiffCallback
) {

    private object DiffCallback : DiffUtil.ItemCallback<Hour>() {
        override fun areItemsTheSame(oldItem: Hour, newItem: Hour):
                Boolean = oldItem.time == newItem.time

        override fun areContentsTheSame(oldItem: Hour, newItem: Hour):
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

class ItemHourViewHolder(private var binding: ItemHourBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(hourData: Hour, weatherCodeMapping: WeatherCodeMapping) {
        binding.hourData = hourData
        binding.weatherCodeMapping = weatherCodeMapping
        binding.executePendingBindings()
    }
}
