package com.example.neoweather.ui.utils

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.neoweather.R
import com.example.neoweather.data.local.model.day.Day
import com.example.neoweather.data.local.model.hour.Hour
import com.example.neoweather.data.local.model.place.Place
import com.example.neoweather.data.local.model.preferences.Preferences
import com.example.neoweather.data.remote.geocoding.model.GeoLocation
import com.example.neoweather.ui.home.adapter.HomeTabAdapter
import com.example.neoweather.ui.home.weather.adapter.daily.DailyForecastAdapter
import com.example.neoweather.ui.home.weather.adapter.hourly.HourlyForecastAdapter
import com.example.neoweather.ui.search.adapter.SearchListAdapter

@BindingAdapter("apiStatusImage")
fun bindStatusImage(statusImage: ImageView, status: ApiStatus?) {
    when(status) {
        ApiStatus.LOADING -> {
            statusImage.setImageResource(R.drawable.loading_animation)
            statusImage.visibility = View.VISIBLE
        }
        ApiStatus.ERROR -> {
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

@BindingAdapter(
    value = ["previousListSize", "currentListSize", "syncPreviousSize"],
    requireAll = true)
fun checkIfPlaceWasAdded(
    viewPager: ViewPager2,
    previousListSize: Int,
    currentListSize: Int?,
    syncPreviousSize: () -> Unit
) {
    if (currentListSize == null || previousListSize == currentListSize)
        return

    viewPager.setCurrentItem(currentListSize, true)
    syncPreviousSize()
}

@BindingAdapter(value = ["hourList", "preferences"], requireAll = true)
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
fun bindDayList(recyclerView: RecyclerView, dayList: List<Day>?, preferences: Preferences?
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

@BindingAdapter("areNotificationsEnabled")
fun displayIntervalMenuConditionally(
    menuHolder: ConstraintLayout,
    notifications: Boolean?
) {
    when (notifications) {
        true -> menuHolder.visibility = View.VISIBLE
        false -> menuHolder.visibility = View.GONE
        else -> menuHolder.visibility = View.GONE
    }
}