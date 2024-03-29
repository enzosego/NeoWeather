package com.example.neoweather.data.remote.weather

import com.example.neoweather.data.remote.utils.HttpRoutes
import com.example.neoweather.data.remote.utils.KtorClientBuilder
import com.example.neoweather.data.remote.weather.model.NeoWeatherModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class NeoWeatherApiImpl(
    private val client: HttpClient
) : NeoWeatherApi {

    override suspend fun getWeather(
        lat: Double,
        lon: Double,
        timezone: String
    ): NeoWeatherModel =
        client.get(HttpRoutes.FORECAST) {
            parameter("latitude", "$lat")
            parameter("longitude", "$lon")
            parameter("timezone", timezone)
        }.body()

    companion object {
        fun create(): NeoWeatherApi =
            NeoWeatherApiImpl(KtorClientBuilder().client)
    }
}