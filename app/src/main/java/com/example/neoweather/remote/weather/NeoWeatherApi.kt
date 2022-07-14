package com.example.neoweather.remote.weather

import com.example.neoweather.remote.weather.model.NeoWeatherModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.open-meteo.com/v1/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface NeoWeatherApiService {
    @GET("forecast?current_weather=true&hourly=temperature_2m,weathercode" +
            "&daily=temperature_2m_max,temperature_2m_min,precipitation_sum," +
            "rain_sum,weathercode,sunrise,sunset,winddirection_10m_dominant,windspeed_10m_max")
    suspend fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double,
        @Query("timezone") timezone: String
    ): NeoWeatherModel
}

object NeoWeatherApi {
    val retrofitService: NeoWeatherApiService by lazy {
        retrofit.create(NeoWeatherApiService::class.java)
    }
}