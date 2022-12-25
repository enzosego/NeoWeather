package com.example.neoweather.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NeoWeatherViewModelFactory(private val application: Application)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NeoWeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NeoWeatherViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}