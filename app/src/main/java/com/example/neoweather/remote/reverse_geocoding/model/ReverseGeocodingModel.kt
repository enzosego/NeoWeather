package com.example.neoweather.remote.reverse_geocoding.model

import kotlinx.serialization.Serializable

@Serializable
data class ReverseGeocodingModel(
    val address: ReverseGeocodingAddress
)