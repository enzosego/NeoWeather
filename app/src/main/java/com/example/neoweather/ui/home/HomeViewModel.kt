package com.example.neoweather.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.neoweather.remote.geocoding.model.GeoLocation
import com.example.neoweather.repository.NeoWeatherRepository
import com.example.neoweather.ui.utils.ApiStatus
import com.example.neoweather.workers.NotificationUtils.LATITUDE_PARAM
import com.example.neoweather.workers.NotificationUtils.LONGITUDE_PARAM
import com.example.neoweather.workers.NotificationUtils.NOTIFICATION_WORK_NAME
import com.example.neoweather.workers.ShowCurrentWeatherNotificationWorker
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class HomeViewModel(
    private val repository: NeoWeatherRepository
) : ViewModel() {

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus> = _status

    private val _currentTabNum = MutableLiveData(0)
    val currentTabNum: LiveData<Int> = _currentTabNum

    val dailyData = repository.dailyDataList

    val hourlyData = repository.hourlyDataList

    val currentWeather = repository.currentWeatherList

    val preferences = repository.preferences

    val placesList = repository.placesList

    val currentListSize: LiveData<Int> = Transformations.map(placesList) { it.size }

    val previousListSize = MutableLiveData<Int>()


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

    fun enqueueWeatherNotification(context: Context, lat: Double, lon: Double) {
        val workManager = WorkManager.getInstance(context)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val weatherNotificationRequest =
            PeriodicWorkRequestBuilder<ShowCurrentWeatherNotificationWorker>(
                repeatInterval = 1,
                TimeUnit.HOURS
            )
                .setConstraints(constraints)
                .setInputData(createInputData(lat, lon))
                .build()

        workManager.enqueueUniquePeriodicWork(
            NOTIFICATION_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            weatherNotificationRequest
        )
    }

    private fun createInputData(lat: Double, lon: Double): Data {
        val builder = Data.Builder()
        builder.putDouble(LATITUDE_PARAM, lat)
        builder.putDouble(LONGITUDE_PARAM, lon)
        return builder.build()
    }
}