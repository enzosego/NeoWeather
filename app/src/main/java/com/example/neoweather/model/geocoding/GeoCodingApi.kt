package com.example.neoweather.model.geocoding

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://geocoding-api.open-meteo.com/v1/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface GeoCodingApiService {
    @GET("search?")
    suspend fun getLocation(
        @Query("name") cityName: String
    ): GeoCodingModel
}

object GeoCodingApi {
    val retrofitService: GeoCodingApiService by lazy {
        retrofit.create(GeoCodingApiService::class.java)
    }
}