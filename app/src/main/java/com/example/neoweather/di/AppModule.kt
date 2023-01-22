package com.example.neoweather.di

import android.content.Context
import androidx.room.Room
import com.example.neoweather.data.local.NeoWeatherDatabase
import com.example.neoweather.data.remote.geocoding.GeoCodingApiImpl
import com.example.neoweather.data.remote.reverse_geocoding.ReverseGeoCodingApiImpl
import com.example.neoweather.data.remote.weather.NeoWeatherApiImpl
import com.example.neoweather.data.repository.PreferencesRepository
import com.example.neoweather.data.repository.WeatherDataRepository
import com.example.neoweather.data.workers.location.UpdateLocationInDatabaseWorker
import com.example.neoweather.domain.use_case.*
import com.example.neoweather.domain.use_case.day_detail.DayDetailUseCases
import com.example.neoweather.domain.use_case.day_detail.GetDaysByHoursUseCase
import com.example.neoweather.domain.use_case.day_detail.GetSunTimingUseCase
import com.example.neoweather.domain.use_case.search.CallGeoLocationApiUseCase
import com.example.neoweather.domain.use_case.home.*
import com.example.neoweather.domain.use_case.settings.SettingsUseCases
import com.example.neoweather.ui.days.day_detail.DayDetailViewModel
import com.example.neoweather.ui.home.HomeViewModel
import com.example.neoweather.ui.search.SearchViewModel
import com.example.neoweather.ui.settings.SettingsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {

    // DataSources
    single { createDatabase(androidContext()) }

    factory { NeoWeatherApiImpl.create() }
    factory { GeoCodingApiImpl.create() }
    factory { ReverseGeoCodingApiImpl.create() }

    // Repositories
    factory { Dispatchers.IO }
    singleOf(::WeatherDataRepository)
    singleOf(::PreferencesRepository)

    // WorKManager
    workerOf(::UpdateLocationInDatabaseWorker)

    // Domain
    factory { CoroutineScope(Dispatchers.Main) }
    factoryOf(::HomeUseCases)
    factoryOf(::MakeGeoLocationInstanceUseCase)
    factoryOf(::RefreshPlaceWeatherUserCase)
    factoryOf(::InsertOrUpdatePlaceUseCase)
    factoryOf(::DeletePlaceUseCase)
    factoryOf(::FormatTempUnitUseCase)
    factoryOf(::FormatSpeedUnitUseCase)
    factoryOf(::FormatPrecipitationSumUseCase)

    factoryOf(::DayDetailUseCases)
    factoryOf(::GetDaysByHoursUseCase)
    factoryOf(::FindCurrentHourIndexUseCase)
    factoryOf(::GetSunTimingUseCase)

    factoryOf(::SettingsUseCases)
    factoryOf(::UpdateUnitsPreferencesUseCase)
    factoryOf(::UpdateDataPreferencesUseCase)

    factoryOf(::CallGeoLocationApiUseCase)

    // ViewModels
    viewModelOf(::HomeViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::DayDetailViewModel)
}

private fun createDatabase(context: Context): NeoWeatherDatabase =
    Room.databaseBuilder(
        context,
        NeoWeatherDatabase::class.java,
        "neoweather_database"
    )
        .createFromAsset("database/neo_weather.db")
        .build()