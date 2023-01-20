package com.example.neoweather.domain.model

import com.example.neoweather.data.local.model.hour.Hour

data class DayByHours(
    val dayOfWeek: Int,
    val dayOfMonth: String,
    val monthNum: Int,
    val hourList: List<Hour>
)