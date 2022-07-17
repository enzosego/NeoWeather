package com.example.neoweather.remote.geocoding

import com.example.neoweather.util.Utils.ApiBuilder
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://geocoding-api.open-meteo.com/v1/"

interface GeoCodingApiService {
    @GET("search?")
    suspend fun getLocation(
        @Query("name") cityName: String
    ): GeoCodingModel
}

object GeoCodingApi {
    val retrofitService: GeoCodingApiService by lazy {
        ApiBuilder(BASE_URL).retrofit
            .create(GeoCodingApiService::class.java)
    }
}