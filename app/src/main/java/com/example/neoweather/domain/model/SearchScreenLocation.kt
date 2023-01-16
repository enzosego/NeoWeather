package com.example.neoweather.domain.model

data class SearchScreenLocation(
    val placeName: String,
    val country: String,
    val latitude: Long,
    val longitude: Long
)