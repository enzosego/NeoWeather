package com.example.neoweather.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.neoweather.R
import com.example.neoweather.data.model.current.CurrentWeather
import com.example.neoweather.data.model.day.Day
import com.example.neoweather.data.model.hour.Hour
import com.example.neoweather.data.model.preferences.Preferences
import com.example.neoweather.ui.home.DailyForecastAdapter
import com.example.neoweather.ui.home.HourlyForecastAdapter
import com.example.neoweather.viewmodel.NeoWeatherApiStatus

@BindingAdapter("apiStatusImage")
fun bindStatusImage(statusImageView: ImageView, status: NeoWeatherApiStatus?) {
    when(status) {
        NeoWeatherApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
        }
        NeoWeatherApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
        else -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
    }
}

@BindingAdapter(value = ["bind:currentWeather", "bind:preferences"], requireAll = false)
fun bindDescription(textView: TextView,
                    currentWeather: CurrentWeather?,
                    preferences: Preferences?) {
    textView.text =
        when (currentWeather) {
            null ->
                ""
            else ->
                when(textView.id) {
                    R.id.weather_main ->
                        WeatherCodeMapping.description[currentWeather.weatherCode]
                    R.id.temp ->
                        WeatherUnits.getTempUnit(
                            currentWeather.temperature,
                            preferences!!.isFahrenheitEnabled)
                    else ->
                        WeatherUnits.getHourFromTime(currentWeather.time)
                }
        }
}

@BindingAdapter(value = ["bind:hourlyData", "bind:preferences"], requireAll = true)
fun bindHourList(recyclerView: RecyclerView,
                 hourData: List<Hour>?,
                 preferences: Preferences?) {

    val adapter = recyclerView.adapter as HourlyForecastAdapter
    adapter.submitList(hourData)
    adapter.submitPreferences(preferences)
}

@BindingAdapter(value = ["bind:dailyData", "bind:preferences"], requireAll = true)
fun bindDayList(recyclerView: RecyclerView,
                dayData: List<Day>?,
                preferences: Preferences?) {

    val adapter = recyclerView.adapter as DailyForecastAdapter
    adapter.submitList(dayData)
    adapter.submitPreferences(preferences)
}