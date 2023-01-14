package com.example.neoweather.di

import android.content.Context
import androidx.room.Room
import com.example.neoweather.data.local.NeoWeatherDatabase
import com.example.neoweather.data.remote.geocoding.GeoCodingApiImpl
import com.example.neoweather.data.remote.reverse_geocoding.ReverseGeoCodingApiImpl
import com.example.neoweather.data.remote.weather.NeoWeatherApiImpl
import com.example.neoweather.data.repository.PreferencesRepository
import com.example.neoweather.data.repository.WeatherDataRepository
import com.example.neoweather.data.workers.UpdateCurrentLocationInDatabaseWorker
import com.example.neoweather.domain.use_case.EnqueueWorkersUseCase
import com.example.neoweather.ui.home.HomeViewModel
import com.example.neoweather.ui.search.SearchViewModel
import com.example.neoweather.ui.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    single { createDatabase(androidContext()) }

    factory { NeoWeatherApiImpl.create() }
    factory { GeoCodingApiImpl.create() }
    factory { ReverseGeoCodingApiImpl.create() }

    singleOf(::PreferencesRepository)
    singleOf(::WeatherDataRepository)

    workerOf(::UpdateCurrentLocationInDatabaseWorker)
    factoryOf(::EnqueueWorkersUseCase)

    viewModelOf(::HomeViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::SearchViewModel)
}

private fun createDatabase(context: Context): NeoWeatherDatabase =
    Room.databaseBuilder(
        context,
        NeoWeatherDatabase::class.java,
        "neoweather_database"
    )
        .createFromAsset("database/neo_weather.db")
        .build()