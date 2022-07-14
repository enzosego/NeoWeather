package com.example.neoweather.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.neoweather.databinding.ItemHourBinding
import com.example.neoweather.util.WeatherCodeMapping
import com.example.neoweather.data.model.hour.Hour

class HourlyForecastAdapter : ListAdapter<Hour, ItemHourViewHolder>(
    DiffCallback
) {

    private val listSizeLimit = 24

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
            ItemHourBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemHourViewHolder, position: Int) {
        val hourData = getItem(position)
        holder.bind(hourData, WeatherCodeMapping)
    }

    override fun getItemCount(): Int =
        if (currentList.isNotEmpty())
            listSizeLimit
        else
            0
}

class ItemHourViewHolder(private var binding: ItemHourBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(hourData: Hour, weatherCodeMapping: WeatherCodeMapping) {
        binding.hourData = hourData
        binding.weatherCodeMapping = weatherCodeMapping
        binding.executePendingBindings()
    }
}
