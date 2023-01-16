package com.example.neoweather.ui.home.weather.adapter.daily

import com.example.neoweather.data.local.model.day.Day

class DayListener(val clickListener: (day: Day) -> Unit) {
    fun onClick(day: Day) = clickListener(day)
}