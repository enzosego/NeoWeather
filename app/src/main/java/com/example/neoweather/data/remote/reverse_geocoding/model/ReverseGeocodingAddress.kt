package com.example.neoweather.data.remote.reverse_geocoding.model

import kotlinx.serialization.Serializable

@Serializable
data class ReverseGeocodingAddress(
    val city: String = "",
    val state: String,
    val country: String
)

fun ReverseGeocodingAddress.getName(): String =
    city.ifEmpty { state }