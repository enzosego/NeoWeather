package com.example.neoweather.remote.geocoding

import com.example.neoweather.data.model.place.Place
import com.squareup.moshi.Json
import java.time.Instant
import java.util.*

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

fun GeoLocation.asDatabaseModel(placeId: Int): Place =
    Place(
        id = placeId,
        name = name,
        latitude = latitude,
        longitude = longitude,
        country = country,
        countryCode = countryCode,
        timezone = timezone,
    )