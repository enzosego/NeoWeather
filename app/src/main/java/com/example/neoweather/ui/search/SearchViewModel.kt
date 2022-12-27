package com.example.neoweather.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neoweather.remote.geocoding.GeoCodingApiImpl
import com.example.neoweather.remote.geocoding.model.GeoLocation
import com.example.neoweather.repository.NeoWeatherRepository
import com.example.neoweather.ui.utils.NeoWeatherApiStatus
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: NeoWeatherRepository
) : ViewModel() {

    private val _locationList = MutableLiveData<List<GeoLocation>>()
    val locationList: LiveData<List<GeoLocation>> = _locationList

    private val _status = MutableLiveData<NeoWeatherApiStatus>()
    val status: LiveData<NeoWeatherApiStatus> = _status

    fun updateLocationList(cityName: String) {
        viewModelScope.launch {
            val newList = GeoCodingApiImpl.create().getLocation(cityName)
                .results
            _locationList.postValue(newList)
        }
    }

    fun onLocationClicked(location: GeoLocation) {
        _locationList.postValue(listOf())
        insertPlace(location)
    }

    private fun insertPlace(location: GeoLocation) =
        viewModelScope.launch {
            try {
                _status.value = NeoWeatherApiStatus.LOADING

                repository.updateOrInsertPlace(location)

                _status.value = NeoWeatherApiStatus.DONE
            } catch (e: Exception) {
                _status.value = NeoWeatherApiStatus.ERROR
            }
        }
}