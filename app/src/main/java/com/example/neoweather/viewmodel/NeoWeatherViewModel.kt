package com.example.neoweather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neoweather.model.NeoWeatherApi
import com.example.neoweather.model.NeoWeatherModel
import kotlinx.coroutines.launch

enum class NeoWeatherApiStatus { LOADING, DONE, ERROR }

class NeoWeatherViewModel : ViewModel() {

    private val _status = MutableLiveData<NeoWeatherApiStatus>()
    val status: LiveData<NeoWeatherApiStatus> = _status

    private val _weatherInfo = MutableLiveData<NeoWeatherModel>()
    val weatherInfo: LiveData<NeoWeatherModel> = _weatherInfo

    init {
        getWeatherData()
    }

    private fun getWeatherData() {
        viewModelScope.launch {
            try {
                _status.postValue(NeoWeatherApiStatus.LOADING)

                val newWeatherForecast = NeoWeatherApi.retrofitService.getWeather()
                _weatherInfo.postValue(newWeatherForecast)

                _status.postValue(NeoWeatherApiStatus.DONE)
            } catch (e: Exception) {
                _status.postValue(NeoWeatherApiStatus.ERROR)
                _weatherInfo.value = null
            }
        }
    }
}