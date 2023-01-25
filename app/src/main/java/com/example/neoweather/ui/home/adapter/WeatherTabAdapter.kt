package com.example.neoweather.ui.home.adapter

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.neoweather.domain.model.PlaceModel
import com.example.neoweather.ui.home.weather.WeatherTabFragment

class WeatherTabAdapter(
    fragmentManger: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManger, lifecycle) {

    private var placesList: List<PlaceModel>? = null

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<PlaceModel>?) {
        placesList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = placesList?.size ?: 0

    override fun createFragment(position: Int): Fragment =
        WeatherTabFragment.newInstance(position)
}