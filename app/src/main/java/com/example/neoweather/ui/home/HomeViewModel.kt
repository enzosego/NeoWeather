package com.example.neoweather.ui.home

import android.util.Log
import androidx.lifecycle.*
import androidx.work.ExistingPeriodicWorkPolicy
import com.example.neoweather.data.remote.geocoding.model.GeoLocation
import com.example.neoweather.data.repository.WeatherDataRepository
import com.example.neoweather.data.repository.PreferencesRepository
import com.example.neoweather.ui.utils.ApiStatus
import com.example.neoweather.domain.use_case.EnqueueWorkersUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel(
    private val weatherDataRepository: WeatherDataRepository,
    private val preferencesRepository: PreferencesRepository,
    private val enqueueWorkersUseCase: EnqueueWorkersUseCase
) : ViewModel() {

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus> = _status

    private val _currentTabNum = MutableLiveData(0)
    val currentTabNum: LiveData<Int> = _currentTabNum

    val placesList = weatherDataRepository.placesList

    val dailyData = weatherDataRepository.dailyDataList

    val hourlyData = weatherDataRepository.hourlyDataList

    val currentWeather = weatherDataRepository.currentWeatherList

    val currentListSize: LiveData<Int> = Transformations.map(placesList) { it.size }

    val previousListSize = MutableLiveData<Int>()

    val preferences = preferencesRepository.preferences

    val areNotificationsEnabled: LiveData<Boolean> = Transformations.map(preferences) {
        it.areNotificationsEnabled
    }

    private var currentJob: Job? = null

    fun refreshPlaceWeather(id: Int) {
        currentJob?.cancel()
        currentJob =
            viewModelScope.launch {
                try {
                    _status.postValue(ApiStatus.LOADING)

                    weatherDataRepository.refreshPlaceWeather(id)

                    _status.postValue(ApiStatus.DONE)
                } catch (e: Exception) {
                    Log.d("DEBUG", "Error: $e")
                    _status.postValue(ApiStatus.ERROR)
                }
            }
    }

    fun insertOrUpdatePlace(location: GeoLocation) {
        viewModelScope.launch {
            try {
                _status.postValue(ApiStatus.LOADING)

                weatherDataRepository.updateOrInsertPlace(location)

                _status.postValue(ApiStatus.DONE)
            } catch (e: Exception) {
                _status.postValue(ApiStatus.ERROR)
            }
        }
    }

    fun deletePlace(placeId: Int) {
        viewModelScope.launch {
            weatherDataRepository.deletePlace(placeId)
        }
    }

    fun setCurrentTabNum(newValue: Int) {
        _currentTabNum.postValue(newValue)
    }

    fun syncPreviousSize() {
        previousListSize.value = currentListSize.value!!
    }

    fun enqueueWorkers() {
        enqueueWorkersUseCase(
            interval = preferences.value?.notificationsInterval ?: 1L,
            ExistingPeriodicWorkPolicy.KEEP
        )
    }

    fun setBackgroundPermissionDenied() {
        val newPreferences = preferences.value!!.copy(
            backgroundPermissionDenied = true
        )
        viewModelScope.launch { preferencesRepository.updatePreferences(newPreferences) }
    }
}