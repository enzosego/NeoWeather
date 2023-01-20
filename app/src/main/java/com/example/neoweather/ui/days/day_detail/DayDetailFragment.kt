package com.example.neoweather.ui.days.day_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neoweather.R
import com.example.neoweather.databinding.FragmentDayDetailBinding
import com.example.neoweather.ui.days.day_detail.adapter.DayDetailHourListAdapter
import com.example.neoweather.ui.settings.observeOnce
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class DayDetailFragment(
    private val position: Int,
    private val placeId: Int
) : Fragment() {

    private val viewModel: DayDetailViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDayDetailBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.getDays(placeId)

        viewModel.days.observeOnce(viewLifecycleOwner) { list ->
            with(list[position]) {
                binding.dayOfTheWeek.text = getDayOfWeekString(dayOfWeek)
                binding.date.text = getDateInfoString(dayOfMonth, monthNum)
                binding.hourListRecyclerview.adapter = DayDetailHourListAdapter(hourList)
            }
        }
        binding.hourListRecyclerview.layoutManager =
            LinearLayoutManager(requireContext())

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
        private var PLACE_ID_ARG = "place_id_arg"
        private var POSITION_ARG = "position_arg"
        @JvmStatic
        fun newInstance(position: Int, placeId: Int) =
            DayDetailFragment(position, placeId).apply {
                arguments = Bundle().apply {
                    putInt(POSITION_ARG, position)
                    putInt(PLACE_ID_ARG, placeId)
                }
            }
    }
}
