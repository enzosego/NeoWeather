package com.example.neoweather.model

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
private const val API_KEY = "a63f2c379197334015f2d7a78700d620"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface NeoWeatherApiService {
    @GET("weather?q=london&appid=$API_KEY")
    suspend fun getCurrentWeather(): CurrentWeatherModel

    @GET("forecast?q=london&appid=$API_KEY")
    suspend fun getForecastWeather(): ForecastWeatherModel
}

object NeoWeatherApi {
    val retrofitService: NeoWeatherApiService by lazy {
        retrofit.create(NeoWeatherApiService::class.java)
    }
}