package com.example.neoweather.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.neoweather.data.NeoWeatherDatabase
import com.example.neoweather.remote.geocoding.GeoCodingApi
import com.example.neoweather.remote.geocoding.GeoLocation
import com.example.neoweather.repository.NeoWeatherRepository
import com.example.neoweather.util.Utils.NeoWeatherApiStatus
import com.example.neoweather.util.Utils.TAG
import com.example.neoweather.util.WeatherUnits
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NeoWeatherViewModel(application: Application)
    : ViewModel() {

    private val _status = MutableLiveData<NeoWeatherApiStatus>()
    val status: LiveData<NeoWeatherApiStatus> = _status

    private val neoWeatherRepository =
        NeoWeatherRepository(NeoWeatherDatabase.getDatabase(application))

    private val _locationList = MutableLiveData<List<GeoLocation>>()
    val locationList: LiveData<List<GeoLocation>> = _locationList

    val dailyData = neoWeatherRepository.dailyDataList

    val hourlyData = neoWeatherRepository.hourlyDataList

    val currentWeather = neoWeatherRepository.currentWeatherList

    val preferences = neoWeatherRepository.preferences

    val placesList = neoWeatherRepository.placesList

    init {
        viewModelScope.launch {
            neoWeatherRepository.initiatePreferences()
        }
    }

    fun refreshPlaceData(location: GeoLocation?, placeId: Int?) {
        viewModelScope.launch {
            try {
                _status.postValue(NeoWeatherApiStatus.LOADING)

                neoWeatherRepository.refreshPlaceData(location, placeId)

                _status.postValue(NeoWeatherApiStatus.DONE)
            } catch (e: Exception) {
                Log.d(TAG, "Error: $e")
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

    fun updateSearchList(cityName: String) {
        viewModelScope.launch {
            val newList = GeoCodingApi.retrofitService.getLocation(cityName)
                .results
            _locationList.postValue(newList)
        }
    }

    fun onLocationClicked(location: GeoLocation) {
        _locationList.postValue(listOf())
        refreshPlaceData(location, null)
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