package com.example.neoweather.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neoweather.model.*
import kotlinx.coroutines.launch

enum class NeoWeatherApiStatus { LOADING, DONE, ERROR }

class NeoWeatherViewModel : ViewModel() {

    private val _status = MutableLiveData<NeoWeatherApiStatus>()
    val status: LiveData<NeoWeatherApiStatus> = _status

    private val _currentWeather = MutableLiveData<CurrentWeather>()
    val currentWeather: LiveData<CurrentWeather> = _currentWeather

    private val _hourlyForecast = MutableLiveData<HourlyForecast>()
    val hourlyForecast: LiveData<HourlyForecast> = _hourlyForecast

    private val _dailyForecast = MutableLiveData<DailyForecast>()
    val dailyForecast: LiveData<DailyForecast> = _dailyForecast

    val codeMapping = WeatherCodeMapping.mapping

    init {
        getWeatherData()
    }

    private fun getWeatherData() {
        viewModelScope.launch {
            try {
                _status.postValue(NeoWeatherApiStatus.LOADING)

                val newWeatherInstance = NeoWeatherApi.retrofitService.getWeather()
                _currentWeather.postValue(newWeatherInstance.currentWeather)
                _hourlyForecast.postValue(newWeatherInstance.hourlyForecast)
                _dailyForecast.postValue(newWeatherInstance.dailyForecast)

                _status.postValue(NeoWeatherApiStatus.DONE)
            } catch (e: Exception) {
                _status.postValue(NeoWeatherApiStatus.ERROR)
                _currentWeather.value = null
                _hourlyForecast.value = null
                _dailyForecast.value = null
            }
        }
    }

    fun parseToString(float: Float): String = float.toString()
}