package com.example.neoweather.ui.home.weather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neoweather.databinding.FragmentWeatherBinding
import com.example.neoweather.ui.home.weather.adapter.DailyForecastAdapter
import com.example.neoweather.ui.home.weather.adapter.HourlyForecastAdapter
import com.example.neoweather.util.Utils.TAG
import com.example.neoweather.util.WeatherCodeMapping
import com.example.neoweather.util.WeatherUnits
import com.example.neoweather.viewmodel.NeoWeatherViewModel
import com.example.neoweather.viewmodel.NeoWeatherViewModelFactory

class WeatherTabFragment(private val position: Int) : Fragment() {

    private val viewModel: NeoWeatherViewModel by activityViewModels {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        NeoWeatherViewModelFactory(activity.application)
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

        viewModel.refreshPlaceData(null, position)

        return binding.root
    }

    companion object {
        private var POSITION_ARG = "position_arg"
        @JvmStatic
        fun newInstance(position: Int) = WeatherTabFragment(position).apply {
            arguments = Bundle().apply {
                putInt(POSITION_ARG, position)
            }
        }
    }
}