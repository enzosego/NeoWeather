package com.example.neoweather.ui.home.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.neoweather.domain.model.PlaceModel
import com.example.neoweather.ui.home.weather.WeatherTabFragment

class HomeTabAdapter(activity: FragmentActivity)
    : FragmentStateAdapter(activity) {

    private var placesList: List<PlaceModel>? = null

    fun submitList(newList: List<PlaceModel>?) {
        placesList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = placesList?.size ?: 0

    override fun createFragment(position: Int): Fragment =
        WeatherTabFragment.newInstance(position)
}