package com.example.neoweather.ui.home.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neoweather.R
import com.example.neoweather.databinding.FragmentWeatherBinding
import com.example.neoweather.domain.use_case.FindCurrentHourIndexUseCase
import com.example.neoweather.ui.askForBackgroundLocationPermission
import com.example.neoweather.ui.home.HomeFragmentDirections
import com.example.neoweather.ui.home.weather.adapter.daily.DailyForecastAdapter
import com.example.neoweather.ui.home.weather.adapter.hourly.HourlyForecastAdapter
import com.example.neoweather.ui.home.HomeViewModel
import com.example.neoweather.ui.isBackgroundPermissionGranted
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WeatherTabFragment(private val position: Int) : Fragment(), KoinComponent {

    private val viewModel: HomeViewModel by activityViewModel()

    private val findCurrentHourIndex: FindCurrentHourIndexUseCase by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWeatherBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.hourlyForecastRecyclerView.adapter = HourlyForecastAdapter()
        binding.hourlyForecastRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.hourlyForecastRecyclerView.isNestedScrollingEnabled = false

        binding.dailyForecastRecyclerView.adapter = DailyForecastAdapter { dayPosition ->
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToDaysFragment(position, dayPosition)
            )
        }
        binding.dailyForecastRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.dailyForecastRecyclerView.isNestedScrollingEnabled = false

        binding.viewModel = viewModel
        binding.position = position
        binding.findCurrentHourIndex = findCurrentHourIndex

        binding.placeName.setOnClickListener {
            if (viewModel.isGpsLocation(position)
                && !isBackgroundPermissionGranted(requireContext())
            ) {
                val messageResource = R.string.permanent_location_toggle_message
                requireContext().askForBackgroundLocationPermission(messageResource)
                return@setOnClickListener
            }
            viewModel.setPreferredLocation(position)
        }

        return binding.root
    }

    companion object {
        private var POSITION_ARG = "position_arg"
        @JvmStatic
        fun newInstance(position: Int) =
            WeatherTabFragment(position).apply {
                arguments = Bundle().apply {
                    putInt(POSITION_ARG, position)
                }
        }
    }
}