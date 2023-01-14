package com.example.neoweather.data.remote.geocoding

import com.example.neoweather.data.remote.geocoding.model.GeoCodingModel
import com.example.neoweather.data.remote.utils.HttpRoutes
import com.example.neoweather.data.remote.utils.KtorClientBuilder
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class GeoCodingApiImpl(
    private val client: HttpClient
) : GeoCodingApi {

    override suspend fun getLocation(cityName: String): GeoCodingModel =
        client.get(HttpRoutes.GEOCODING_QUERY) {
            parameter("name",  cityName)
        }.body()

    companion object {
        fun create(): GeoCodingApi =
            GeoCodingApiImpl(KtorClientBuilder().client)
    }
}