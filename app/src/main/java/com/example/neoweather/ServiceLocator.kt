package com.example.neoweather

import com.example.neoweather.data.NeoWeatherDatabase
import com.example.neoweather.repository.NeoWeatherRepository

object ServiceLocator {

    @Volatile
    var repository: NeoWeatherRepository? = null

    private val lock = Any()

    fun provideRepository(database: NeoWeatherDatabase): NeoWeatherRepository =
        synchronized(lock) {
            return repository ?: createRepository(database)
        }

    private fun createRepository(database: NeoWeatherDatabase): NeoWeatherRepository {
        val newRepository = NeoWeatherRepository(database)
        repository = newRepository
        return newRepository
    }
}