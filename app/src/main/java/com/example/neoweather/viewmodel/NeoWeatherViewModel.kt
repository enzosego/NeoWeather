package com.example.neoweather.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.neoweather.model.database.day.Day
import com.example.neoweather.model.database.day.DayDao
import com.example.neoweather.model.database.hour.Hour
import com.example.neoweather.model.database.hour.HourDao
import com.example.neoweather.model.remote.geocoding.*
import com.example.neoweather.model.remote.weather.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class NeoWeatherApiStatus { LOADING, DONE, ERROR }

class NeoWeatherViewModel(
    private val dayDao: DayDao,
    private val hourDao: HourDao)
    : ViewModel() {

    private val _status = MutableLiveData<NeoWeatherApiStatus>()
    val status: LiveData<NeoWeatherApiStatus> = _status

    private val _currentWeather = MutableLiveData<CurrentWeather>()
    val currentWeather: LiveData<CurrentWeather> = _currentWeather

    private val _currentLocation = MutableLiveData<GeoLocation>()
    //val currentLocation: LiveData<GeoLocation> = _currentLocation

    val hourlyData: LiveData<List<Hour>> =
        hourDao.getAllHours().asLiveData()

    val dailyData: LiveData<List<Day>> =
        dayDao.getAllDays().asLiveData()

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
                updateHourlyData(newWeatherInstance.hourlyForecast)
                updateDailyData(newWeatherInstance.dailyForecast)

                _status.postValue(NeoWeatherApiStatus.DONE)
            } catch (e: Exception) {
                Log.d("error", e.toString())
                _status.postValue(NeoWeatherApiStatus.ERROR)
                _currentWeather.value = null
            }
        }
    }

    private fun updateHourlyData(hourlyForecast: HourlyForecast) {
        with(hourlyForecast) {
            for (i in weatherCode.indices) {
                val newHour = Hour(
                    time = time[i].subSequence(11, 16).toString(),
                    temp = hourlyTemp[i].toString(),
                    weatherCode = weatherCode[i]
                )
                if (hourlyData.value.isNullOrEmpty())
                    insertHour(newHour)
                else
                    updateHour(newHour)
            }
        }
    }

    private fun updateDailyData(dailyForecast: DailyForecast) {
        with(dailyForecast) {
            for (i in time.indices) {
                val newDay = Day(
                    time = time[i],
                    sunrise = sunrise[i],
                    sunset = sunset[i],
                    maxTemp = maxTemp[i].toString(),
                    minTemp = minTemp[i].toString(),
                    precipitationSum = precipitationSum[i].toString(),
                    rainSum = rainSum[i].toString(),
                    windDirectionDominant = windDirectionDominant[i].toString(),
                    windSpeedMax = windSpeedMax[i].toString(),
                    weatherCode = weatherCode[i]
                )
                if (dailyData.value.isNullOrEmpty())
                    insertDay(newDay)
                else
                    updateDay(newDay)
            }
        }
    }

    private fun insertHour(hour: Hour) {
        viewModelScope.launch(Dispatchers.IO) {
            hourDao.insert(hour)
        }
    }

    private fun updateHour(hour: Hour) {
        viewModelScope.launch(Dispatchers.IO) {
            hourDao.update(hour)
        }
    }

    private fun insertDay(day: Day) {
        viewModelScope.launch(Dispatchers.IO) {
            dayDao.insert(day)
        }
    }

    private fun updateDay(day: Day) {
        viewModelScope.launch(Dispatchers.IO) {
            dayDao.update(day)
        }
    }
}

class NeoWeatherViewModelFactory(private val dayDao: DayDao,private val hourDao: HourDao)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NeoWeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NeoWeatherViewModel(dayDao, hourDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}