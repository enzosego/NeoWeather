package com.example.neoweather.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neoweather.model.CurrentWeatherModel
import com.example.neoweather.model.ForecastWeatherModel
import com.example.neoweather.model.NeoWeatherApi
import kotlinx.coroutines.launch

enum class NeoWeatherApiStatus { LOADING, DONE, ERROR }

class NeoWeatherViewModel : ViewModel() {

    private val _status = MutableLiveData<NeoWeatherApiStatus>()
    val status: LiveData<NeoWeatherApiStatus> = _status

    private val _currentWeather = MutableLiveData<CurrentWeatherModel>()
    val currentWeather: LiveData<CurrentWeatherModel> = _currentWeather

    private val _forecastWeather = MutableLiveData<ForecastWeatherModel>()
    val forecastWeather: LiveData<ForecastWeatherModel> = _forecastWeather

    init {
        getWeatherData()
    }

    private fun getWeatherData() {
        viewModelScope.launch {
            try {
                _status.postValue(NeoWeatherApiStatus.LOADING)

                val newCurrentWeather = NeoWeatherApi.retrofitService.getCurrentWeather()
                _currentWeather.postValue(newCurrentWeather)

                val newForecastWeather = NeoWeatherApi.retrofitService.getForecastWeather()
                _forecastWeather.postValue(newForecastWeather)

                Log.d("ViewModel", _currentWeather.value?.name ?: "no value")

                _status.postValue(NeoWeatherApiStatus.DONE)
            } catch (e: Exception) {
                _status.postValue(NeoWeatherApiStatus.ERROR)
                _currentWeather.value = null
            }
        }
    }
}