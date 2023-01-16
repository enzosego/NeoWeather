package com.example.neoweather.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neoweather.domain.model.SearchScreenLocation
import com.example.neoweather.domain.use_case.search.CallGeoLocationApiUseCase
import com.example.neoweather.ui.utils.ApiStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel(
    private val callGeoLocationApi: CallGeoLocationApiUseCase
) : ViewModel() {

    private val _locationList = MutableLiveData<List<SearchScreenLocation>>()
    val locationList: LiveData<List<SearchScreenLocation>> = _locationList

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus> = _status

    private var currentJob: Job? = null

    fun updateLocationList(placeName: String) {
        currentJob?.cancel()
        currentJob =
            viewModelScope.launch {
                try {
                    _status.value = ApiStatus.LOADING

                    val newList = callGeoLocationApi(placeName)
                    _locationList.postValue(newList)

                    _status.value = ApiStatus.DONE
                } catch (e: Exception) {
                    _status.value = ApiStatus.ERROR
                    _locationList.value = emptyList()
                    Log.d("SearchViewModel", e.cause.toString())
                }
            }
    }

    fun overrideApiStatus() {
        _status.value = ApiStatus.DONE
    }
}