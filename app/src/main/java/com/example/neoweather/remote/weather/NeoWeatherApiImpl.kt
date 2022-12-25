package com.example.neoweather.remote.weather

import com.example.neoweather.remote.utils.HttpRoutes
import com.example.neoweather.remote.utils.KtorClientBuilder
import com.example.neoweather.remote.weather.model.NeoWeatherModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class NeoWeatherApiImpl(
    private val client: HttpClient
) : NeoWeatherApi {

    override suspend fun getWeather(
        lat: Double,
        long: Double,
        timezone: String
    ): NeoWeatherModel =
        client.get(HttpRoutes.FORECAST) {
            parameter("latitude", "$lat")
            parameter("longitude", "$long")
            parameter("timezone", timezone)
        }.body()

    companion object {
        fun create(): NeoWeatherApi =
            NeoWeatherApiImpl(KtorClientBuilder().client)
    }
}