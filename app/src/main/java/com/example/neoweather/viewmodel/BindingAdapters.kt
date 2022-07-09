package com.example.neoweather.viewmodel

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.neoweather.R
import com.example.neoweather.view.DailyForecastAdapter
import com.example.neoweather.view.HourlyForecastAdapter

@BindingAdapter("apiStatus")
fun bindStatus(statusImageView: ImageView, status: NeoWeatherApiStatus?) {
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

@BindingAdapter("hourlyData")
fun bindHourList(recyclerView: RecyclerView, hourData: List<HourData>?) {
    val adapter = recyclerView.adapter as HourlyForecastAdapter
    adapter.submitList(hourData)
}

@BindingAdapter("dailyData")
fun bindDayList(recyclerView: RecyclerView, dayData: List<DayData>?) {
    val adapter = recyclerView.adapter as DailyForecastAdapter
    adapter.submitList(dayData)
}
