package com.example.neoweather.remote.geocoding.model

import kotlinx.serialization.Serializable

@Serializable
data class GeoCodingModel(
    val results: List<GeoLocation>
)