package com.example.neoweather.remote.reverse_geocoding.model

import kotlinx.serialization.Serializable

@Serializable
data class ReverseGeocodingModel(
    val address: ReverseGeocodingAddress
)

@Serializable
data class ReverseGeocodingAddress(
    val city: String = "",
    val state: String,
    val country: String
)

fun ReverseGeocodingAddress.getName(): String =
    city.ifEmpty { state }