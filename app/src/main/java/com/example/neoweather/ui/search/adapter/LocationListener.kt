package com.example.neoweather.ui.search.adapter

import com.example.neoweather.remote.geocoding.GeoLocation

class LocationListener(val clickListener: (location: GeoLocation) -> Unit) {
    fun onClick(location: GeoLocation) = clickListener(location)
}