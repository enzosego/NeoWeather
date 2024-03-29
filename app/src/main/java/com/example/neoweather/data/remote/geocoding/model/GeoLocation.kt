package com.example.neoweather.data.remote.geocoding.model

import com.example.neoweather.data.local.model.place.Place
import com.example.neoweather.domain.model.SearchScreenLocation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeoLocation(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    @SerialName("country_code")
    val countryCode: String,
    val country: String,
    val timezone: String
)

fun GeoLocation.asDatabaseModel(placeId: Int, isGpsLocation: Boolean): Place =
    Place(
        id = placeId,
        name = name,
        latitude = latitude,
        longitude = longitude,
        country = country,
        countryCode = countryCode,
        timezone = timezone,
        isGpsLocation = isGpsLocation
    )

fun GeoLocation.asDomainModel(): SearchScreenLocation =
    SearchScreenLocation(
        placeName = name,
        country = country,
        latitude = latitude.toLong(),
        longitude = longitude.toLong()
    )

fun GeoLocation.isGpsLocation(): Boolean =
    name.isEmpty()