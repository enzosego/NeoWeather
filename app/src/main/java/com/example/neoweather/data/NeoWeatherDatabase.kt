package com.example.neoweather.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.neoweather.data.model.current.CurrentWeather
import com.example.neoweather.data.model.current.CurrentWeatherDao
import com.example.neoweather.data.model.day.Day
import com.example.neoweather.data.model.day.DayDao
import com.example.neoweather.data.model.hour.Hour
import com.example.neoweather.data.model.hour.HourDao

@Database(entities = [Day::class, Hour::class, CurrentWeather::class]
    , version = 1, exportSchema = false)
abstract class NeoWeatherDatabase : RoomDatabase() {

    abstract val dayDao: DayDao
    abstract val hourDao: HourDao
    abstract val currentWeatherDao: CurrentWeatherDao

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
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                //return instance
                instance
            }
        }
    }
}