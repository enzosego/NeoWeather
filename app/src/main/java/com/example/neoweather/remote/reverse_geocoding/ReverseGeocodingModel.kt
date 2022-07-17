package com.example.neoweather.remote.reverse_geocoding

data class ReverseGeocodingModel(
    val address: ReverseGeocodingAddress
)

data class ReverseGeocodingAddress(
    val city: String = "",
    val state: String
)