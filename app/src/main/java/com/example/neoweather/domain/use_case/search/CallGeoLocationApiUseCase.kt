package com.example.neoweather.domain.use_case.search

import com.example.neoweather.data.remote.geocoding.GeoCodingApi
import com.example.neoweather.data.remote.geocoding.model.asDomainModel
import com.example.neoweather.domain.model.SearchScreenLocation

class CallGeoLocationApiUseCase(private val geoCodingSource: GeoCodingApi) {

    suspend operator fun invoke(placeName: String): List<SearchScreenLocation> =
        geoCodingSource.getLocation(placeName).results
            .map { it.asDomainModel() }
}