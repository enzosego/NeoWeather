package com.example.neoweather.data.local.converters

import androidx.room.TypeConverter
import com.example.neoweather.data.local.model.day.Day
import com.example.neoweather.data.local.model.hour.Hour
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

//@ProvidedTypeConverter
object RoomConverters {

    private val jsonParser = GsonParser(Gson())

    @TypeConverter
    @JvmStatic
    fun toHoursJson(hours: List<Hour>): String {
        return jsonParser.toJson(
            hours,
            object : TypeToken<ArrayList<Hour>>(){}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromHoursJson(json: String): List<Hour> {
        return jsonParser.fromJson<ArrayList<Hour>>(
            json,
            object : TypeToken<ArrayList<Hour>>(){}.type
        ) ?: emptyList()
    }

    @TypeConverter
    @JvmStatic
    fun toDaysJson(days: List<Day>): String {
        return jsonParser.toJson(
            days,
            object : TypeToken<ArrayList<Day>>(){}.type
        ) ?: "[]"
    }

    @TypeConverter
    @JvmStatic
    fun fromDaysJson(json: String): List<Day> {
        return jsonParser.fromJson<ArrayList<Day>>(
            json,
            object : TypeToken<ArrayList<Day>>(){}.type
        ) ?: emptyList()
    }
}