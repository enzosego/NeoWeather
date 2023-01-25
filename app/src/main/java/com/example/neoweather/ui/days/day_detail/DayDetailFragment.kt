package com.example.neoweather.ui.days.day_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neoweather.R
import com.example.neoweather.databinding.FragmentDayDetailBinding
import com.example.neoweather.domain.use_case.day_detail.GetSunTimingUseCase
import com.example.neoweather.ui.days.day_detail.adapter.DayDetailHourListAdapter
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DayDetailFragment : Fragment(), KoinComponent {

    private val viewModel: DayDetailViewModel by activityViewModel()

    private val getSunTiming: GetSunTimingUseCase by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDayDetailBinding.inflate(inflater)

        val position = requireArguments().getInt(POSITION_ARG)
        val placeId = requireArguments().getInt(PLACE_ID_ARG)

        with(viewModel.days.value!![position]) {
            binding.dayOfTheWeek.text = getDayOfWeekString(dayOfWeek)
            binding.date.text = getDateInfoString(dayOfMonth, monthNum)
            binding.hourListRecyclerview.adapter = DayDetailHourListAdapter(
                hourList,
                getSunriseTime = { hour ->
                    getSunTiming(hour, placeId) { day -> day.sunrise }
                },
                getSunsetTime = { hour ->
                    getSunTiming(hour, placeId) { day -> day.sunset }
                }
            )
        }
        binding.hourListRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        if (position == 0)
            binding.hourListRecyclerview
                .scrollToPosition(viewModel.getCurrentHourIndex(position))

        return binding.root
    }

    private fun getDayOfWeekString(num: Int): String {
        val resource = when(num) {
            1 -> R.string.sunday
            2 -> R.string.monday
            3 -> R.string.tuesday
            4 -> R.string.wednesday
            5 -> R.string.thursday
            6 -> R.string.friday
            else -> R.string.saturday
        }
        return getString(resource)
    }

    private fun getDateInfoString(dayOfMonth: String, monthNum: Int): String {
        val months = resources.getStringArray(R.array.months)
        val month = months[monthNum]
        return getString(R.string.date_info, dayOfMonth, month)
    }

    companion object {
        private var POSITION_ARG = "position_arg"
        private var PLACE_ID_ARG = "place_id_arg"
        @JvmStatic
        fun newInstance(position: Int, placeId: Int) =
            DayDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(POSITION_ARG, position)
                    putInt(PLACE_ID_ARG, placeId)
                }
            }
    }
}