package com.example.neoweather.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.neoweather.databinding.ItemLocationBinding
import com.example.neoweather.data.remote.geocoding.model.GeoLocation
import com.example.neoweather.domain.model.SearchScreenLocation

class SearchListAdapter(private val clickListener: LocationListener)
    : ListAdapter<SearchScreenLocation, ItemLocationViewHolder>(DiffCallback) {

    private object DiffCallback : DiffUtil.ItemCallback<SearchScreenLocation>() {

        override fun areItemsTheSame(
            oldItem: SearchScreenLocation, newItem: SearchScreenLocation): Boolean =
            oldItem.placeName == newItem.placeName && oldItem.country == newItem.country

        override fun areContentsTheSame(
            oldItem: SearchScreenLocation, newItem: SearchScreenLocation): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemLocationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ItemLocationViewHolder(
            ItemLocationBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemLocationViewHolder, position: Int) {
        val location = getItem(position)
        holder.bind(location, clickListener)
    }
}