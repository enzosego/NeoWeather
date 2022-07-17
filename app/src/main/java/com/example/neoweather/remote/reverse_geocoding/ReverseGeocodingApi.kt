package com.example.neoweather.remote.reverse_geocoding

import com.example.neoweather.util.Utils.accessToken
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://us1.locationiq.com/v1/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ReverseGeocodingApiService {
    @GET("reverse?key=${accessToken}&format=json")
    suspend fun getLocationName(
        @Query("lat") lat: Double,
        @Query("lon") long: Double
    ): ReverseGeocodingModel
}

object ReverseGeoCodingApi {
    val retrofitService: ReverseGeocodingApiService by lazy {
        retrofit.create(ReverseGeocodingApiService::class.java)
    }
}
