package com.example.neoweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neoweather.model.NeoWeatherModel
import com.example.neoweather.model.NeoWeatherApi
import kotlinx.coroutines.launch

enum class NeoWeatherApiStatus { LOADING, DONE, ERROR }

class NeoWeatherViewModel : ViewModel() {

    private val _status = MutableLiveData<NeoWeatherApiStatus>()
    val status: LiveData<NeoWeatherApiStatus> = _status

    private val _weatherData = MutableLiveData<NeoWeatherModel>()
    val weatherData: LiveData<NeoWeatherModel> = _weatherData

    init {
        getWeatherData()
    }

    private fun getWeatherData() {
        viewModelScope.launch {
            try {
                _status.postValue(NeoWeatherApiStatus.LOADING)

                val newCurrentWeather = NeoWeatherApi.retrofitService.getWeather()
                _weatherData.postValue(newCurrentWeather)

                _status.postValue(NeoWeatherApiStatus.DONE)
            } catch (e: Exception) {
                _status.postValue(NeoWeatherApiStatus.ERROR)
                _weatherData.value = null
            }
        }
    }

    fun parseToString(float: Float): String = float.toString()
}