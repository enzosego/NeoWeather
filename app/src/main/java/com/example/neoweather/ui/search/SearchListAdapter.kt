package com.example.neoweather.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.neoweather.databinding.ItemLocationBinding
import com.example.neoweather.remote.geocoding.GeoLocation

class SearchListAdapter(private val clickListener: LocationListener)
    : ListAdapter<GeoLocation, ItemLocationViewHolder>(DiffCallback) {

    private val listSizeLimit = 6

    private object DiffCallback : DiffUtil.ItemCallback<GeoLocation>() {
        override fun areItemsTheSame(oldItem: GeoLocation, newItem: GeoLocation):
                Boolean = oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: GeoLocation, newItem: GeoLocation):
                Boolean = oldItem.latitude == newItem.latitude
                && oldItem.longitude == newItem.longitude
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

    override fun getItemCount(): Int =
        if (currentList.isNotEmpty())
            listSizeLimit
        else 0
}

class ItemLocationViewHolder(private var binding: ItemLocationBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(geoLocation: GeoLocation, clickListener: LocationListener) {
        binding.location = geoLocation
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }
}

class LocationListener(val clickListener: (location: GeoLocation) -> Unit) {
    fun onClick(location: GeoLocation) = clickListener(location)
}