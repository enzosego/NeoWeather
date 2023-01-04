package com.example.neoweather.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neoweather.remote.geocoding.GeoCodingApiImpl
import com.example.neoweather.remote.geocoding.model.GeoLocation
import com.example.neoweather.repository.NeoWeatherRepository
import com.example.neoweather.ui.utils.ApiStatus
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: NeoWeatherRepository
) : ViewModel() {

    private val _locationList = MutableLiveData<List<GeoLocation>>()
    val locationList: LiveData<List<GeoLocation>> = _locationList

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus> = _status

    fun updateLocationList(cityName: String) {
        viewModelScope.launch {
            try {
                _status.value = ApiStatus.LOADING

                val newList = GeoCodingApiImpl.create().getLocation(cityName)
                    .results
                _locationList.postValue(newList)

                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                _locationList.value = emptyList()
                Log.d("SearchViewModel", e.cause.toString())
            }
        }
    }

    fun cleanApiStatus() {
        _status.value = ApiStatus.DONE
    }

    fun onLocationClicked(location: GeoLocation) {
        _locationList.postValue(listOf())
        insertPlace(location)
    }

    private fun insertPlace(location: GeoLocation) =
        viewModelScope.launch {
            try {
                repository.updateOrInsertPlace(location)
            } catch (e: Exception) {
                Log.d("DEBUG", "${e.message}")
            }
        }
}