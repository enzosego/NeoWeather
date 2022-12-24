package com.example.neoweather.ui.search.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.neoweather.databinding.ItemLocationBinding
import com.example.neoweather.remote.geocoding.GeoLocation

class ItemLocationViewHolder(private var binding: ItemLocationBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(geoLocation: GeoLocation, clickListener: LocationListener) {
        binding.location = geoLocation
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }
}