package com.example.neoweather.domain.model

import com.example.neoweather.data.local.model.hour.Hour

data class DayByHours(
    val dayOfWeek: String,
    val dayNum: String,
    val hourList: List<Hour>
)