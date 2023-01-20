package com.example.neoweather.ui.days.day_detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.neoweather.data.local.model.hour.Hour
import com.example.neoweather.databinding.ItemHourDetailBinding

class DayDetailHourListAdapter(
    private val hourList: List<Hour>
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
        holder.bind(hourData)
    }

    override fun getItemCount(): Int = hourList.size
}

