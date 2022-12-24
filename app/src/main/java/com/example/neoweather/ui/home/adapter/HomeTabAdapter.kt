package com.example.neoweather.ui.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.neoweather.data.model.place.Place
import com.example.neoweather.ui.home.weather.WeatherTabFragment

class HomeTabAdapter(activity: FragmentActivity)
    : FragmentStateAdapter(activity) {

    private var placesList: List<Place>? = null

    fun submitList(newList: List<Place>?) {
        placesList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = placesList?.size ?: 0

    override fun createFragment(position: Int): Fragment =
        WeatherTabFragment.newInstance(position)
}