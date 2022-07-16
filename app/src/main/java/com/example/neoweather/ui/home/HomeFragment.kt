package com.example.neoweather.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neoweather.databinding.FragmentHomeBinding
import com.example.neoweather.viewmodel.NeoWeatherViewModel
import com.example.neoweather.viewmodel.NeoWeatherViewModelFactory

class HomeFragment : Fragment() {

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
        val binding = FragmentHomeBinding.inflate(inflater)

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

        return binding.root
    }
}
