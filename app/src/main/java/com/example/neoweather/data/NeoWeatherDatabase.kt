package com.example.neoweather.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.neoweather.data.converters.RoomConverters
import com.example.neoweather.data.model.current.CurrentWeather
import com.example.neoweather.data.model.current.CurrentWeatherDao
import com.example.neoweather.data.model.day.DaysDao
import com.example.neoweather.data.model.day.DaysEntity
import com.example.neoweather.data.model.hour.HoursDao
import com.example.neoweather.data.model.hour.HoursEntity
import com.example.neoweather.data.model.place.Place
import com.example.neoweather.data.model.place.PlaceDao
import com.example.neoweather.data.model.preferences.Preferences
import com.example.neoweather.data.model.preferences.PreferencesDao

@Database(
    entities = [
        DaysEntity::class,
        HoursEntity::class,
        CurrentWeather::class,
        Preferences::class,
        Place::class
    ],
    version = 1,
    exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class NeoWeatherDatabase : RoomDatabase() {

    abstract val placeDao: PlaceDao
    abstract val daysDao: DaysDao
    abstract val hoursDao: HoursDao
    abstract val currentWeatherDao: CurrentWeatherDao
    abstract val preferencesDao: PreferencesDao

    companion object {
        @Volatile
        private var INSTANCE: NeoWeatherDatabase? = null

        fun getDatabase(context: Context): NeoWeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NeoWeatherDatabase::class.java,
                    "neoweather_database"
                )
                    .createFromAsset("database/neo_weather.db")
                    .build()
                INSTANCE = instance
                //return instance
                instance
            }
        }
    }
}