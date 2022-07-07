package com.example.neoweather.model

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.open-meteo.com/v1/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface NeoWeatherApiService {
    @GET("forecast?latitude=52.52&longitude=13.41&current_weather=true" +
            "&hourly=temperature_2m&hourly=weathercode&timezone=UTC" +
            "&daily=temperature_2m_max&daily=temperature_2m_min&daily=precipitation_sum" +
            "&daily=rain_sum&daily=weathercode&daily=sunrise&daily=sunset" +
            "&daily=winddirection_10m_dominant&daily=windspeed_10m_max")
    suspend fun getWeather(): NeoWeatherModel
}

object NeoWeatherApi {
    val retrofitService: NeoWeatherApiService by lazy {
        retrofit.create(NeoWeatherApiService::class.java)
    }
}