package com.example.neoweather.remote.utils

import com.example.neoweather.util.Utils

object HttpRoutes {

    //Weather Forecast Route

    private const val FORECAST_BASE_URL = "https://api.open-meteo.com/v1"

    private const val CURRENT_WEATHER = "current_weather=true"

    private const val HOURLY_FORECAST = "hourly=temperature_2m,weathercode"

    private const val DAILY_FORECAST = "daily=temperature_2m_max,temperature_2m_min," +
            "precipitation_sum,weathercode,winddirection_10m_dominant,windspeed_10m_max"

    const val FORECAST =
        "$FORECAST_BASE_URL/forecast?$CURRENT_WEATHER&$HOURLY_FORECAST&$DAILY_FORECAST"

    //GeoCoding Route

    private const val GEOCODING_BASE_URL = "https://geocoding-api.open-meteo.com/v1"

    const val GEOCODING_QUERY = "$GEOCODING_BASE_URL/search?"

    // Reverse GeoCoding Route

    private const val REVERSE_BASE_URL = "https://us1.locationiq.com/v1"

    const val REVERSE_QUERY = "$REVERSE_BASE_URL/reverse?key=${Utils.ACCESS_TOKEN}&format=json"
}