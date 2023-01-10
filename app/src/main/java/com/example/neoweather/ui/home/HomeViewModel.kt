package com.example.neoweather.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.neoweather.remote.geocoding.model.GeoLocation
import com.example.neoweather.repository.NeoWeatherRepository
import com.example.neoweather.ui.utils.ApiStatus
import com.example.neoweather.workers.GetCurrentLocationWorker
import com.example.neoweather.workers.NotificationUtils
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class HomeViewModel(
    private val repository: NeoWeatherRepository
) : ViewModel() {

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus> = _status

    private val _currentTabNum = MutableLiveData(0)
    val currentTabNum: LiveData<Int> = _currentTabNum

    val placesList = repository.placesList

    val dailyData = repository.dailyDataList

    val hourlyData = repository.hourlyDataList

    val currentWeather = repository.currentWeatherList

    val currentListSize: LiveData<Int> = Transformations.map(placesList) { it.size }

    val previousListSize = MutableLiveData<Int>()

    val preferences = repository.preferences

    val areNotificationsEnabled: LiveData<Boolean> = Transformations.map(preferences) { pref ->
        pref.areNotificationsEnabled
    }

    fun insertOrUpdatePlace(location: GeoLocation) {
        viewModelScope.launch {
            try {
                _status.postValue(ApiStatus.LOADING)

                repository.updateOrInsertPlace(location)

                _status.postValue(ApiStatus.DONE)
            } catch (e: Exception) {
                _status.postValue(ApiStatus.ERROR)
            }
        }
    }

    fun refreshPlaceWeather(id: Int) {
        viewModelScope.launch {
            try {
                _status.postValue(ApiStatus.LOADING)

                repository.refreshPlaceWeather(id)

                _status.postValue(ApiStatus.DONE)
            } catch (e: Exception) {
                Log.d("DEBUG", "Error: $e")
                _status.postValue(ApiStatus.ERROR)
            }
        }
    }

    fun deletePlace(placeId: Int) {
        viewModelScope.launch {
            repository.deletePlace(placeId)
        }
    }

    fun setCurrentTabNum(newValue: Int) {
        _currentTabNum.postValue(newValue)
    }

    fun syncPreviousSize() {
        previousListSize.value = currentListSize.value!!
    }

    fun enqueueWorkers(context: Context) {
        val getCurrentLocationWorker =
            PeriodicWorkRequestBuilder<GetCurrentLocationWorker>(
                preferences.value!!.notificationsInterval,
                TimeUnit.HOURS
            )
                .addTag(NotificationUtils.GET_CURRENT_LOCATION_WORK_TAG)
                .build()

        WorkManager
            .getInstance(context)
            .enqueueUniquePeriodicWork(
                NotificationUtils.GET_CURRENT_LOCATION_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                getCurrentLocationWorker
            )
    }

    fun setBackgroundPermissionDenied() {
        val newPreferences = preferences.value!!.copy(
            backgroundPermissionDenied = true
        )
        viewModelScope.launch { repository.updatePreferences(newPreferences) }
    }
}