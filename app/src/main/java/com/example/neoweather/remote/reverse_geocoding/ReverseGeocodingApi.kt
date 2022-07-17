package com.example.neoweather.remote.reverse_geocoding

import com.example.neoweather.util.Utils.ApiBuilder
import com.example.neoweather.util.Utils.accessToken
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://us1.locationiq.com/v1/"

interface ReverseGeocodingApiService {
    @GET("reverse?key=${accessToken}&format=json")
    suspend fun getLocationName(
        @Query("lat") lat: Double,
        @Query("lon") long: Double
    ): ReverseGeocodingModel
}

object ReverseGeoCodingApi {
    val retrofitService: ReverseGeocodingApiService by lazy {
        ApiBuilder(BASE_URL).retrofit
            .create(ReverseGeocodingApiService::class.java)
    }
}
