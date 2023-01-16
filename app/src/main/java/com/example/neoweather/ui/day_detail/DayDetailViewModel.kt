package com.example.neoweather.ui.day_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.neoweather.domain.model.DayByHours
import com.example.neoweather.domain.use_case.day_detail.GetDayDetailUseCase

class DayDetailViewModel(
    private val getDayDetailUseCase: GetDayDetailUseCase
) : ViewModel() {

    private val _dayByHours = MutableLiveData<DayByHours>()
    val dayByHours: LiveData<DayByHours> = _dayByHours

    fun getDayDetail(dayNum: Int, position: Int) {
        _dayByHours.value = getDayDetailUseCase(dayNum, position)
    }
}