package com.example.neoweather.domain.use_case.home

import com.example.neoweather.data.remote.geocoding.model.GeoLocation

class MakeGeoLocationInstanceUseCase {

    operator fun invoke(lat: Double, lon: Double, placeName: String? = null): GeoLocation =
        GeoLocation(
            name = placeName ?: "",
            latitude = lat,
            longitude = lon,
            countryCode = "",
            country = "",
            timezone = ""
        )
}