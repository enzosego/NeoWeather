package com.example.neoweather.ui.home.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neoweather.databinding.FragmentWeatherBinding
import com.example.neoweather.ui.home.weather.adapter.daily.DailyForecastAdapter
import com.example.neoweather.ui.home.weather.adapter.hourly.HourlyForecastAdapter
import com.example.neoweather.ui.utils.WeatherUnits
import com.example.neoweather.ui.utils.WeatherCodeMapping
import com.example.neoweather.ui.home.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class WeatherTabFragment(private val position: Int) : Fragment() {

    private val viewModel: HomeViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentWeatherBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.hourlyForecastRecyclerView.adapter = HourlyForecastAdapter()
        binding.hourlyForecastRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.hourlyForecastRecyclerView.isNestedScrollingEnabled = false

        binding.dailyForecastRecyclerView.adapter = DailyForecastAdapter()
        binding.dailyForecastRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
        binding.dailyForecastRecyclerView.isNestedScrollingEnabled = false

        binding.viewModel = viewModel
        binding.position = position
        binding.weatherCodeMapping = WeatherCodeMapping
        binding.weatherUnits = WeatherUnits

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