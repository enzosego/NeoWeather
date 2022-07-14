package com.example.neoweather.remote.geocoding

import com.squareup.moshi.Json

data class GeoCodingModel(
    val results: List<GeoLocation>
)

data class GeoLocation(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    @Json(name = "country_code") val countryCode: String,
    val country: String,
    val timezone: String
)