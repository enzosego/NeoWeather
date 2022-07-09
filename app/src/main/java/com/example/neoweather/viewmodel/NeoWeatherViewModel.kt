package com.example.neoweather.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neoweather.model.geocoding.GeoCodingApi
import com.example.neoweather.model.geocoding.GeoLocation
import com.example.neoweather.model.weather.CurrentWeather
import com.example.neoweather.model.weather.NeoWeatherApi
import com.example.neoweather.model.weather.NeoWeatherModel
import kotlinx.coroutines.launch

enum class NeoWeatherApiStatus { LOADING, DONE, ERROR }

class NeoWeatherViewModel : ViewModel() {

    private val _status = MutableLiveData<NeoWeatherApiStatus>()
    val status: LiveData<NeoWeatherApiStatus> = _status

    private val _currentWeather = MutableLiveData<CurrentWeather>()
    val currentWeather: LiveData<CurrentWeather> = _currentWeather

    private val _hourlyForecast = MutableLiveData<List<HourData>>()
    val hourlyForecast: LiveData<List<HourData>> = _hourlyForecast

    private val _dailyForecast = MutableLiveData<List<DayData>>()
    val dailyForecast: LiveData<List<DayData>> = _dailyForecast

    private val _currentLocation = MutableLiveData<GeoLocation>()
    val currentLocation: LiveData<GeoLocation> = _currentLocation

    init {
        getWeatherData()
    }

    private fun getWeatherData() {
        viewModelScope.launch {
            try {
                _status.postValue(NeoWeatherApiStatus.LOADING)

                val newLocation = GeoCodingApi.retrofitService.getLocation("santa fe")
                _currentLocation.value = newLocation.results[0]

                val newWeatherInstance =
                    NeoWeatherApi.retrofitService.getWeather(
                        _currentLocation.value!!.latitude,
                        _currentLocation.value!!.longitude,
                        _currentLocation.value!!.timezone)

                _currentWeather.postValue(newWeatherInstance.currentWeather)
                mapHourlyForecastValues(newWeatherInstance)
                mapDailyForecastValues(newWeatherInstance)

                _status.postValue(NeoWeatherApiStatus.DONE)
            } catch (e: Exception) {
                Log.d("error", e.toString())
                _status.postValue(NeoWeatherApiStatus.ERROR)
                _currentWeather.value = null
                _hourlyForecast.value = null
                _dailyForecast.value = null
            }
        }
    }

    private fun mapHourlyForecastValues(newWeatherInstance: NeoWeatherModel) {
        val newList = mutableListOf<HourData>()
        with(newWeatherInstance.hourlyForecast) {
            for (i in weatherCode.indices) {
                val newHour = HourData(
                    time[i],
                    hourlyTemp[i].toString(),
                    weatherCode[i]
                )
                newList.add(newHour)
            }
        }
        _hourlyForecast.value = newList
    }

    private fun mapDailyForecastValues(newWeatherInstance: NeoWeatherModel) {
        val newList = mutableListOf<DayData>()
        with(newWeatherInstance.dailyForecast) {
            for (i in time.indices) {
                val newDay = DayData(
                    time[i],
                    sunrise[i],
                    sunset[i],
                    maxTemp[i].toString(),
                    minTemp[i].toString(),
                    precipitationSum[i].toString(),
                    rainSum[i].toString(),
                    windDirectionDominant[i].toString(),
                    windSpeedMax[i].toString(),
                    weatherCode[i]
                )
                newList.add(newDay)
            }
        }
        _dailyForecast.value = newList
    }

    fun parseToString(float: Float): String = float.toString()
}