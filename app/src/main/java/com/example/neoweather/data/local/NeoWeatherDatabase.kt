package com.example.neoweather.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.neoweather.data.local.converters.RoomConverters
import com.example.neoweather.data.local.model.current.CurrentWeather
import com.example.neoweather.data.local.model.current.CurrentWeatherDao
import com.example.neoweather.data.local.model.day.DaysDao
import com.example.neoweather.data.local.model.day.DaysEntity
import com.example.neoweather.data.local.model.hour.HoursDao
import com.example.neoweather.data.local.model.hour.HoursEntity
import com.example.neoweather.data.local.model.place.Place
import com.example.neoweather.data.local.model.place.PlaceDao
import com.example.neoweather.data.local.model.preferences.Preferences
import com.example.neoweather.data.local.model.preferences.PreferencesDao

@Database(
    entities = [
        DaysEntity::class,
        HoursEntity::class,
        CurrentWeather::class,
        Preferences::class,
        Place::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class NeoWeatherDatabase : RoomDatabase() {

    abstract val placeDao: PlaceDao
    abstract val daysDao: DaysDao
    abstract val hoursDao: HoursDao
    abstract val currentWeatherDao: CurrentWeatherDao
    abstract val preferencesDao: PreferencesDao
}