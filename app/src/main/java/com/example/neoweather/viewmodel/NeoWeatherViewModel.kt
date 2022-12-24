package com.example.neoweather.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neoweather.data.NeoWeatherDatabase
import com.example.neoweather.remote.geocoding.GeoCodingApi
import com.example.neoweather.remote.geocoding.GeoLocation
import com.example.neoweather.repository.NeoWeatherRepository
import com.example.neoweather.util.Utils.NeoWeatherApiStatus
import com.example.neoweather.util.Utils.TAG
import kotlinx.coroutines.launch

class NeoWeatherViewModel(application: Application)
    : ViewModel() {

    private val _status = MutableLiveData<NeoWeatherApiStatus>()
    val status: LiveData<NeoWeatherApiStatus> = _status

    private val neoWeatherRepository =
        NeoWeatherRepository(NeoWeatherDatabase.getDatabase(application))

    private val _locationList = MutableLiveData<List<GeoLocation>>()
    val locationList: LiveData<List<GeoLocation>> = _locationList

    private val _currentTabNum = MutableLiveData(0)
    val currentTabNum: LiveData<Int> = _currentTabNum

    private val _previousPlaceListSize = MutableLiveData<Int>()
    val previousPlaceListSize: LiveData<Int> = _previousPlaceListSize

    val dailyData = neoWeatherRepository.dailyDataList

    val hourlyData = neoWeatherRepository.hourlyDataList

    val currentWeather = neoWeatherRepository.currentWeatherList

    val preferences = neoWeatherRepository.preferences

    val placesList = neoWeatherRepository.placesList

    fun insertOrUpdatePlace(location: GeoLocation) {
        viewModelScope.launch {
            try {
                _status.postValue(NeoWeatherApiStatus.LOADING)

                neoWeatherRepository.updateOrInsertPlace(location)

                _status.postValue(NeoWeatherApiStatus.DONE)
            } catch (e: Exception) {
                Log.d(TAG, "Error: $e")
                _status.postValue(NeoWeatherApiStatus.ERROR)
            }
        }
    }

    fun refreshPlaceData(id: Int) {
        viewModelScope.launch {
            try {
                _status.postValue(NeoWeatherApiStatus.LOADING)

                neoWeatherRepository.refreshPlaceData(id)

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
        viewModelScope.launch {
            neoWeatherRepository.updatePreferences(updatedPreferences)
        }
    }

    fun updateSpeedUnit(newValue: Boolean) {
        val updatedPreferences = preferences.value!!.copy(
            isMilesEnabled = newValue
        )
        viewModelScope.launch {
            neoWeatherRepository.updatePreferences(updatedPreferences)
        }
    }

    fun updateRainUnit(newValue: Boolean) {
        val updatedPreferences = preferences.value!!.copy(
            isInchesEnabled = newValue
        )
        viewModelScope.launch {
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
        _previousPlaceListSize.postValue(placesList.value!!.size)
        updatePreviousListSize()
        insertOrUpdatePlace(location)
    }

    fun updatePreviousListSize() {
        _previousPlaceListSize.postValue(placesList.value!!.size)
    }

    fun updateCurrentTabNum(newTabNum: Int) {
        _currentTabNum.postValue(newTabNum)
    }

    fun deletePlace(placeId: Int) {
        viewModelScope.launch {
            neoWeatherRepository.deletePlace(placeId)
        }
    }
}