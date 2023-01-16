package com.example.neoweather.ui.search.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.neoweather.databinding.ItemLocationBinding
import com.example.neoweather.data.remote.geocoding.model.GeoLocation
import com.example.neoweather.domain.model.SearchScreenLocation

class ItemLocationViewHolder(private var binding: ItemLocationBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(location: SearchScreenLocation, clickListener: LocationListener) {
        binding.location = location
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }
}