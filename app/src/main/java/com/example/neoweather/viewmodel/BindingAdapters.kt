package com.example.neoweather.viewmodel

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.neoweather.R
import com.example.neoweather.model.WeatherCodeMapping
import com.example.neoweather.model.database.day.Day
import com.example.neoweather.model.database.hour.Hour
import com.example.neoweather.model.remote.weather.CurrentWeather
import com.example.neoweather.view.home.DailyForecastAdapter
import com.example.neoweather.view.home.HourlyForecastAdapter

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

@BindingAdapter(value = ["bind:status", "bind:currentWeather"], requireAll = true)
fun bindDescription(textView: TextView,
                    status: NeoWeatherApiStatus?,
                    currentWeather: CurrentWeather?) {
    textView.text =
        when(status) {
            NeoWeatherApiStatus.DONE -> {
                when(textView.id) {
                    R.id.weather_main ->
                        WeatherCodeMapping.mapping[currentWeather?.weatherCode]
                    R.id.temp ->
                        currentWeather?.temperature.toString()
                    else ->
                        currentWeather?.time
                }
            }
            else -> ""
        }
}

@BindingAdapter("hourlyData")
fun bindHourList(recyclerView: RecyclerView, hourData: List<Hour>?) {
    val adapter = recyclerView.adapter as HourlyForecastAdapter
    adapter.submitList(hourData)
}

@BindingAdapter("dailyData")
fun bindDayList(recyclerView: RecyclerView, dayData: List<Day>?) {
    val adapter = recyclerView.adapter as DailyForecastAdapter
    adapter.submitList(dayData)
}