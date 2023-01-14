package com.example.neoweather.data.remote.reverse_geocoding

import com.example.neoweather.data.remote.reverse_geocoding.model.ReverseGeocodingModel

interface ReverseGeoCodingApi {
    suspend fun getLocationName(
        lat: Double,
        long: Double
    ): ReverseGeocodingModel
}