package com.example.neoweather.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.example.neoweather.data.local.model.place.asDomainModel
import com.example.neoweather.data.remote.geocoding.model.GeoLocation
import com.example.neoweather.data.repository.WeatherDataRepository
import com.example.neoweather.data.repository.PreferencesRepository
import com.example.neoweather.domain.model.PlaceModel
import com.example.neoweather.ui.utils.ApiStatus
import com.example.neoweather.domain.use_case.home.HomeUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel(
    weatherDataRepository: WeatherDataRepository,
    preferencesRepository: PreferencesRepository,
    private val homeUseCases: HomeUseCases
) : ViewModel() {

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus> = _status

    private val _currentTabNum = MutableLiveData(0)
    val currentTabNum: LiveData<Int> = _currentTabNum

    val placesList: LiveData<List<PlaceModel>> =
        Transformations.map(weatherDataRepository.placesList) { list ->
            list.map { it.asDomainModel() }
        }

    val currentWeather = weatherDataRepository.currentWeatherList

    val dailyData = weatherDataRepository.dailyDataList

    val hourlyData = weatherDataRepository.hourlyDataList

    val currentListSize: LiveData<Int> = Transformations.map(placesList) { it.size }

    val previousListSize = MutableLiveData<Int>()

    val unitsPreferences = preferencesRepository.unitsPreferences

    val dataPreferences = preferencesRepository.dataPreferences

    private var currentJob: Job? = null

    fun refreshPlaceWeather(id: Int) {
        currentJob?.cancel()
        currentJob =
            viewModelScope.launch {
                try {
                    _status.postValue(ApiStatus.LOADING)

                    homeUseCases.refreshPlaceWeather(id)

                    _status.postValue(ApiStatus.DONE)
                } catch (e: Exception) {
                    Log.d("HomeViewModel", "Error: $e")
                    _status.postValue(ApiStatus.ERROR)
                }
            }
    }

    private fun insertOrUpdatePlace(location: GeoLocation) {
        viewModelScope.launch {
            try {
                _status.postValue(ApiStatus.LOADING)

                homeUseCases.insertOrUpdatePlace(location)

                _status.postValue(ApiStatus.DONE)
            } catch (e: Exception) {
                Log.d("HomeViewModel", "Error: $e")
                _status.postValue(ApiStatus.ERROR)
            }
        }
    }

    fun insertPlace(lat: Double, lon: Double, placeName: String? = null) {
        insertOrUpdatePlace(homeUseCases.makeGeoLocationInstance(lat, lon, placeName))
    }

    fun deletePlace(id: Int) {
        homeUseCases.deletePlace(id)
    }

    fun setCurrentTabNum(newValue: Int) {
        _currentTabNum.postValue(newValue)
    }

    fun syncPreviousSize() {
        previousListSize.value = currentListSize.value!!
    }

    fun setBackgroundPermissionDenied() {
        val newPreferences = dataPreferences.value!!.copy(
            backgroundPermissionDenied = true
        )
        homeUseCases.updateDataPreferences(newPreferences)
    }

    fun formatTemp(temp: Double): String =
        homeUseCases.formatTempUnit(temp)

    fun setPreferredLocation(id: Int) {
        if (dataPreferences.value?.preferredLocationId == id)
            return
        Log.d("HomeViewModel", "GOT HERE!")
        val newPreferences = dataPreferences.value!!.copy(
            preferredLocationId = id
        )
        homeUseCases.updateDataPreferences(newPreferences)
    }

    fun scheduleQueueHandler() {
        homeUseCases.scheduleQueueHandler()
    }

    fun isGpsLocation(id: Int): Boolean =
        homeUseCases.checkIfLocationIsGps(id)
}