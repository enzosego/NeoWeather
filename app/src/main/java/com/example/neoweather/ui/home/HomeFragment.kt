package com.example.neoweather.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.neoweather.R
import com.example.neoweather.databinding.FragmentHomeBinding
import com.example.neoweather.ui.home.weather.adapter.WeatherTabAdapter
import com.example.neoweather.util.Utils.TAG
import com.example.neoweather.viewmodel.NeoWeatherViewModel
import com.example.neoweather.viewmodel.NeoWeatherViewModelFactory

class HomeFragment : Fragment(){

    private val viewModel: NeoWeatherViewModel by activityViewModels {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        NeoWeatherViewModelFactory(activity.application)
    }

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.apiStatusHolder.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

        val updateTabNum = object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.updateCurrentTabNum(position)
                viewModel.refreshPlaceData(null, position)
                Log.d(TAG, "Current position: $position")
            }
        }
        binding.homeViewPager.apply {
            post {
                setCurrentItem(viewModel.currentTabNum.value!!, false)
            }
            adapter = WeatherTabAdapter(requireActivity())
            registerOnPageChangeCallback(updateTabNum)
        }

        binding.currentItemNum = binding.homeViewPager.currentItem

        return binding.root
    }
}