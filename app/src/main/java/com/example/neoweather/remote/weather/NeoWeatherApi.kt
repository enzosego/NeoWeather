package com.example.neoweather.remote.weather

import com.example.neoweather.remote.weather.model.NeoWeatherModel
import com.example.neoweather.util.Utils.ApiBuilder
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.open-meteo.com/v1/"

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
        ApiBuilder(BASE_URL).retrofit
            .create(NeoWeatherApiService::class.java)
    }
}