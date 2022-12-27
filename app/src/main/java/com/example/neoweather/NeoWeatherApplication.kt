package com.example.neoweather

import android.app.Application
import com.example.neoweather.data.NeoWeatherDatabase
import com.example.neoweather.repository.NeoWeatherRepository

class NeoWeatherApplication : Application() {

    private val database: NeoWeatherDatabase by lazy { NeoWeatherDatabase.getDatabase(this) }

    val repository: NeoWeatherRepository
        get() = ServiceLocator.provideRepository(database)
}