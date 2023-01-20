package com.example.neoweather.ui.days.day_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neoweather.domain.model.DayByHours
import com.example.neoweather.domain.use_case.day_detail.DayDetailUseCases

class DayDetailViewModel(
    private val dayDetailUseCases: DayDetailUseCases
) : ViewModel() {

    private val _days = MutableLiveData<List<DayByHours>>()
    val days: LiveData<List<DayByHours>> = _days

    fun getDays(placeId: Int) {
        _days.value = dayDetailUseCases.getDaysByHours(placeId)
    }
}