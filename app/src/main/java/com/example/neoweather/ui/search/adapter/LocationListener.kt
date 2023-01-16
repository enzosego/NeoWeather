package com.example.neoweather.ui.search.adapter

import com.example.neoweather.domain.model.SearchScreenLocation

class LocationListener(val clickListener: (location: SearchScreenLocation) -> Unit) {
    fun onClick(location: SearchScreenLocation) = clickListener(location)
}