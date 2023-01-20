package com.example.neoweather.ui.days.day_detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.neoweather.data.local.model.hour.Hour
import com.example.neoweather.databinding.ItemHourDetailBinding
import java.util.Date

class DayDetailHourListAdapter(
    private val hourList: List<Hour>,
    private val getSunriseTime: (hour: Hour) -> Date?,
    private val getSunsetTime: (hour: Hour) -> Date?
) : RecyclerView.Adapter<ItemHourDetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHourDetailViewHolder =
        ItemHourDetailViewHolder(
            ItemHourDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ItemHourDetailViewHolder, position: Int) {
        val hourData = hourList[position]
        holder.bind(hourData, getSunriseTime, getSunsetTime)
    }

    override fun getItemCount(): Int = hourList.size
}

