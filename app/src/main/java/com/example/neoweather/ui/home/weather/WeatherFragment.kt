package com.example.neoweather.ui.home.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neoweather.NeoWeatherApplication
import com.example.neoweather.databinding.FragmentWeatherBinding
import com.example.neoweather.ui.home.weather.adapter.daily.DailyForecastAdapter
import com.example.neoweather.ui.home.weather.adapter.hourly.HourlyForecastAdapter
import com.example.neoweather.ui.utils.WeatherUnits
import com.example.neoweather.ui.utils.WeatherCodeMapping
import com.example.neoweather.ui.home.HomeViewModel
import com.example.neoweather.ui.home.HomeViewModelFactory

class WeatherTabFragment(private val position: Int) : Fragment() {

    private val viewModel: HomeViewModel by activityViewModels {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        HomeViewModelFactory(
            (activity.application as NeoWeatherApplication).repository
        )
    }

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