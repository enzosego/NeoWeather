package com.example.neoweather

import android.app.Application
import com.example.neoweather.data.NeoWeatherDatabase

class NeoWeatherApplication : Application() {
    val database: NeoWeatherDatabase by lazy { NeoWeatherDatabase.getDatabase(this) }
}