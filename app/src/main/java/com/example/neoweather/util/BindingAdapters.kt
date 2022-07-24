package com.example.neoweather.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.neoweather.R
import com.example.neoweather.data.model.day.Day
import com.example.neoweather.data.model.hour.Hour
import com.example.neoweather.data.model.place.Place
import com.example.neoweather.data.model.preferences.Preferences
import com.example.neoweather.remote.geocoding.GeoLocation
import com.example.neoweather.ui.home.weather.adapter.DailyForecastAdapter
import com.example.neoweather.ui.home.weather.adapter.HourlyForecastAdapter
import com.example.neoweather.ui.home.weather.adapter.WeatherTabAdapter
import com.example.neoweather.ui.search.SearchListAdapter
import com.example.neoweather.util.Utils.NeoWeatherApiStatus

@BindingAdapter(
    value = ["apiStatusImage", "placeInfo"],
    requireAll = true)
fun bindStatusImage(
    statusImage: ImageView,
    status: NeoWeatherApiStatus?,
    placeInfo: List<Place>?
) {
    when(status) {
        NeoWeatherApiStatus.LOADING -> {
            statusImage.setImageResource(R.drawable.loading_animation)
            statusImage.visibility = View.VISIBLE
        }
        NeoWeatherApiStatus.ERROR -> {
            statusImage.setImageResource(R.drawable.ic_connection_error)
            statusImage.visibility = View.VISIBLE
        }
        NeoWeatherApiStatus.DONE -> statusImage.visibility = View.GONE
        else -> {
            when (placeInfo) {
                null -> {
                    statusImage.setImageResource(R.drawable.ic_connection_error)
                    statusImage.visibility = View.VISIBLE
                }
                else -> statusImage.visibility = View.GONE
            }
        }
    }
}

@BindingAdapter(
    value = ["apiStatusText", "placeInfo"],
    requireAll = true)
fun bindTextStatus(
    textView: TextView,
    status: NeoWeatherApiStatus?,
    placeInfo: List<Place>?
) {
    textView.visibility = when (status) {
        NeoWeatherApiStatus.ERROR -> View.VISIBLE
        null -> {
            when (placeInfo) {
                null -> View.VISIBLE
                else -> View.GONE
            }
        }
        else -> View.GONE
    }
}

@BindingAdapter("placesList")
fun bindPlacesList(viewPager: ViewPager2, placesList: List<Place>?) {
    val adapter = viewPager.adapter as WeatherTabAdapter
    adapter.submitList(placesList)
}

@BindingAdapter(value = ["hourList", "preferences"])
fun bindHourList(
    recyclerView: RecyclerView,
    hourList: List<Hour>?,
    preferences: Preferences?
) {
    val adapter = recyclerView.adapter as HourlyForecastAdapter
    adapter.submitList(hourList)
    adapter.submitPreferences(preferences)
}

@BindingAdapter(value = ["dayList", "preferences"], requireAll = true)
fun bindDayList(
    recyclerView: RecyclerView,
    dayList: List<Day>?,
    preferences: Preferences?
) {
    val adapter = recyclerView.adapter as DailyForecastAdapter
    adapter.submitList(dayList)
    adapter.submitPreferences(preferences)
}

@BindingAdapter("searchList")
fun bindSearchList(recyclerView: RecyclerView, dayList: List<GeoLocation>?) {
    val adapter = recyclerView.adapter as SearchListAdapter
    adapter.submitList(dayList)
}
