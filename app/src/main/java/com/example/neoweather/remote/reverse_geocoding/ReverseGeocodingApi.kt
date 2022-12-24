package com.example.neoweather.remote.reverse_geocoding

import com.example.neoweather.remote.reverse_geocoding.model.ReverseGeocodingModel

interface ReverseGeoCodingApi {
    suspend fun getLocationName(
        lat: Double,
        long: Double
    ): ReverseGeocodingModel
}