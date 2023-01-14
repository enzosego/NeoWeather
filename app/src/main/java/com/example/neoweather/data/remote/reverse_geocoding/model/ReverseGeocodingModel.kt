package com.example.neoweather.data.remote.reverse_geocoding.model

import kotlinx.serialization.Serializable

@Serializable
data class ReverseGeocodingModel(
    val address: ReverseGeocodingAddress
)