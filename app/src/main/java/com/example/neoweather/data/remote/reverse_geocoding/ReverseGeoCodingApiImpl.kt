package com.example.neoweather.data.remote.reverse_geocoding

import com.example.neoweather.data.remote.reverse_geocoding.model.ReverseGeocodingModel
import com.example.neoweather.data.remote.utils.HttpRoutes
import com.example.neoweather.data.remote.utils.KtorClientBuilder
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class ReverseGeoCodingApiImpl(
    private val client: HttpClient
) : ReverseGeoCodingApi {

    override suspend fun getLocationName(
        lat: Double,
        long: Double
    ): ReverseGeocodingModel =
        client.get(HttpRoutes.REVERSE_QUERY) {
            parameter("lat", lat)
            parameter("lon", long)
        }.body()

    companion object {
        fun create(): ReverseGeoCodingApi =
            ReverseGeoCodingApiImpl(KtorClientBuilder().client)
    }
}