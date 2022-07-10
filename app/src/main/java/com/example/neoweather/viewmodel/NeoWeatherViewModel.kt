package com.example.neoweather.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neoweather.model.geocoding.GeoCodingApi
import com.example.neoweather.model.geocoding.GeoLocation
import com.example.neoweather.model.weather.*
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
                mapDailyForecastValues(newWeatherInstance.dailyForecast)

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

                val currentHour = hourSequenceToInt(newWeatherInstance.currentWeather.time)
                val iteratedHour = hourSequenceToInt(time[i])

                val currentDay = daySequenceToInt(newWeatherInstance.currentWeather.time)
                val iteratedDay = daySequenceToInt(time[i])

                if (currentDay == iteratedDay && currentHour >= iteratedHour)
                    continue

                val newHour = HourData(
                    time[i].subSequence(11, 16).toString(),
                    hourlyTemp[i].toString(),
                    weatherCode[i]
                )
                newList.add(newHour)
                if (newList.size == 24)
                    break
            }
        }
        _hourlyForecast.value = newList
    }

    private fun mapDailyForecastValues(dailyForecast: DailyForecast) {
        val newList = mutableListOf<DayData>()
        with(dailyForecast) {
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

    private fun hourSequenceToInt(input: String): Int =
        input.subSequence(11, 13).toString().toInt()

    private fun daySequenceToInt(input: String): Int =
        input.subSequence(8, 10).toString().toInt()

    fun doubleToString(value: Double): String = value.toString()
}