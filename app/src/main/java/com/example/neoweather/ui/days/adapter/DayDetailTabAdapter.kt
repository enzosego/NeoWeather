package com.example.neoweather.ui.days.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.neoweather.ui.days.day_detail.DayDetailFragment

class DayDetailTabAdapter(
    activity: FragmentActivity,
    private val placeId: Int
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment =
        DayDetailFragment.newInstance(position, placeId)
}