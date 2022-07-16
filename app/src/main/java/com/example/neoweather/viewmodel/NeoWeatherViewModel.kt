package com.example.neoweather.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.neoweather.data.NeoWeatherDatabase
import com.example.neoweather.repository.NeoWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class NeoWeatherApiStatus { LOADING, DONE, ERROR }

class NeoWeatherViewModel(application: Application)
    : ViewModel() {

    private val _status = MutableLiveData<NeoWeatherApiStatus>()
    val status: LiveData<NeoWeatherApiStatus> = _status

    private val neoWeatherRepository =
        NeoWeatherRepository(NeoWeatherDatabase.getDatabase(application))

    val dayList = neoWeatherRepository.dailyData

    val hourList = neoWeatherRepository.hourlyData

    val currentWeather = neoWeatherRepository.currentWeather

    val preferences = neoWeatherRepository.preferences

    val placeInfo = neoWeatherRepository.place

    fun refreshDataFromRepository(locationInfo: Map<String, Double>) {
        viewModelScope.launch {
            try {
                _status.postValue(NeoWeatherApiStatus.LOADING)

                neoWeatherRepository.refreshDatabase(locationInfo)

                _status.postValue(NeoWeatherApiStatus.DONE)
            } catch (e: Exception) {
                Log.d("DEBUG", e.toString())
                _status.postValue(NeoWeatherApiStatus.ERROR)
            }
        }
    }

    fun updateTempUnit(newValue: Boolean) {
        val updatedPreferences = preferences.value!!.copy(
            isFahrenheitEnabled = newValue
        )
        viewModelScope.launch(Dispatchers.IO) {
            neoWeatherRepository.updatePreferences(updatedPreferences)
        }
    }
}

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