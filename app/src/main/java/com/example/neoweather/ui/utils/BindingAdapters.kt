package com.example.neoweather.ui.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.neoweather.R
import com.example.neoweather.data.model.day.Day
import com.example.neoweather.data.model.hour.Hour
import com.example.neoweather.data.model.place.Place
import com.example.neoweather.data.model.preferences.Preferences
import com.example.neoweather.remote.geocoding.model.GeoLocation
import com.example.neoweather.ui.home.adapter.HomeTabAdapter
import com.example.neoweather.ui.home.weather.adapter.daily.DailyForecastAdapter
import com.example.neoweather.ui.home.weather.adapter.hourly.HourlyForecastAdapter
import com.example.neoweather.ui.search.adapter.SearchListAdapter

@BindingAdapter("apiStatusImage")
fun bindStatusImage(statusImage: ImageView, status: NeoWeatherApiStatus?) {
    when(status) {
        NeoWeatherApiStatus.LOADING -> {
            statusImage.setImageResource(R.drawable.loading_animation)
            statusImage.visibility = View.VISIBLE
        }
        NeoWeatherApiStatus.ERROR -> {
            statusImage.setImageResource(R.drawable.ic_connection_error)
            statusImage.visibility = View.VISIBLE
        }
        else -> statusImage.visibility = View.GONE
    }
}

@BindingAdapter("placesList")
fun bindPlacesList(viewPager: ViewPager2, placesList: List<Place>?) {
    val adapter = viewPager.adapter as HomeTabAdapter
    adapter.submitList(placesList)
}

/*
@BindingAdapter(
    value = ["previousPlaceListSize", "list", "syncPreviousSize"],
    requireAll = true)
fun checkIfPlaceWasAdded(
    viewPager: ViewPager2,
    previousPlaceListSize: Int?,
    list: List<Place>?,
    syncPreviousSize: () -> Unit
) {
    if (list == null || previousPlaceListSize == null)
        return
    if (previousPlaceListSize == list.size)
        return

    viewPager.apply {
        post {
            setCurrentItem(list.size, true)
        }
    }
    syncPreviousSize()
}
 */

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

@BindingAdapter(
    value = ["dayList", "preferences"],
    requireAll = true)
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