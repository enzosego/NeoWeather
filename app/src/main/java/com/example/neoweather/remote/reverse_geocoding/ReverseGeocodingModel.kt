package com.example.neoweather.remote.reverse_geocoding

data class ReverseGeocodingModel(
    val address: ReverseGeocodingAddress
)

data class ReverseGeocodingAddress(
    val city: String = "",
    val state: String,
    val country: String
)

fun ReverseGeocodingAddress.getName(): String =
    city.ifEmpty { state }