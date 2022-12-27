package com.example.neoweather.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neoweather.remote.geocoding.model.GeoLocation
import com.example.neoweather.repository.NeoWeatherRepository
import com.example.neoweather.ui.utils.NeoWeatherApiStatus
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: NeoWeatherRepository
) : ViewModel() {

    private val _status = MutableLiveData<NeoWeatherApiStatus>()
    val status: LiveData<NeoWeatherApiStatus> = _status

    val dailyData = repository.dailyDataList

    val hourlyData = repository.hourlyDataList

    val currentWeather = repository.currentWeatherList

    val preferences = repository.preferences

    val placesList = repository.placesList

    fun insertOrUpdatePlace(location: GeoLocation) {
        viewModelScope.launch {
            try {
                _status.postValue(NeoWeatherApiStatus.LOADING)

                repository.updateOrInsertPlace(location)

                _status.postValue(NeoWeatherApiStatus.DONE)
            } catch (e: Exception) {
                _status.postValue(NeoWeatherApiStatus.ERROR)
            }
        }
    }

    fun refreshPlaceData(id: Int) {
        viewModelScope.launch {
            try {
                _status.postValue(NeoWeatherApiStatus.LOADING)

                repository.refreshPlaceData(id)

                _status.postValue(NeoWeatherApiStatus.DONE)
            } catch (e: Exception) {
                Log.d("DEBUG", "Error: $e")
                _status.postValue(NeoWeatherApiStatus.ERROR)
            }
        }
    }

    fun deletePlace(placeId: Int) {
        viewModelScope.launch {
            repository.deletePlace(placeId)
        }
    }
}