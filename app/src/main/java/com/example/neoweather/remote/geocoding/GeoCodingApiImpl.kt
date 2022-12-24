package com.example.neoweather.remote.geocoding

import com.example.neoweather.remote.geocoding.model.GeoCodingModel
import com.example.neoweather.remote.utils.HttpRoutes
import com.example.neoweather.remote.utils.KtorClientBuilder
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
            GeoCodingApiImpl(
                KtorClientBuilder(
                    isLoggingEnabled = true,
                    logTag = "geocoding-api"
                ).client
            )
    }
}