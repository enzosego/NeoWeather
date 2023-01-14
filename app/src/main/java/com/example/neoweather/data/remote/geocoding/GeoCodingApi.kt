package com.example.neoweather.data.remote.geocoding

import com.example.neoweather.data.remote.geocoding.model.GeoCodingModel

interface GeoCodingApi {
    suspend fun getLocation(cityName: String): GeoCodingModel
}