package com.example.neoweather.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.neoweather.R
import com.example.neoweather.util.WeatherCodeMapping
import com.example.neoweather.data.model.current.CurrentWeather
import com.example.neoweather.data.model.day.Day
import com.example.neoweather.data.model.hour.Hour
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

@BindingAdapter("currentWeather")
fun bindDescription(textView: TextView,
                    currentWeather: CurrentWeather?) {
    textView.text =
        when (currentWeather) {
            null ->
                ""
            else ->
                when(textView.id) {
                    R.id.weather_main ->
                        WeatherCodeMapping.mapping[currentWeather.weatherCode]
                    R.id.temp ->
                        currentWeather.temperature.toString()
                    else ->
                        currentWeather.time
                }
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