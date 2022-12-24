package com.example.neoweather.remote.geocoding

import com.example.neoweather.remote.geocoding.model.GeoCodingModel

interface GeoCodingApi {
    suspend fun getLocation(cityName: String): GeoCodingModel
}