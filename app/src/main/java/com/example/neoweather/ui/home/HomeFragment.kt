package com.example.neoweather.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.neoweather.databinding.FragmentHomeBinding
import com.example.neoweather.ui.home.adapter.WeatherTabAdapter
import com.example.neoweather.ui.settings.observeOnce
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class HomeFragment : Fragment(){

    private val viewModel: HomeViewModel by activityViewModel()

    private lateinit var args: HomeFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(layoutInflater)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        val refreshDataAndSetTabNum = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.setCurrentTabNum(position)
                viewModel.refreshPlaceWeather(position)
            }
        }
        binding.homeViewPager.apply {
            doOnPreDraw {
                visibility = View.GONE
                setCurrentItem(viewModel.currentTabNum.value ?: 0, false)
            }
            postDelayed({visibility = View.VISIBLE}, 100)

            registerOnPageChangeCallback(refreshDataAndSetTabNum)
            adapter = WeatherTabAdapter(childFragmentManager, lifecycle)
        }
        viewModel.currentListSize.observe(viewLifecycleOwner) { size ->
            if (size != null && viewModel.previousListSize.value == null)
                viewModel.syncPreviousSize()
        }
        viewModel.dataPreferences.observeOnce(viewLifecycleOwner) { preferences ->
            if (preferences != null)
                viewModel.scheduleQueueHandler()
        }

        return binding.root
    }

    override fun onAttach(context: Context) {
        args = HomeFragmentArgs.fromBundle(requireArguments())
        if (didUserClickOnNewLocation()) {
            addNewLocation()
            args = HomeFragmentArgs("", 999L, 999L)
        }
        super.onAttach(context)
    }

    private fun addNewLocation() {
        val lat = args.latitude.toDouble()
        val lon = args.longitude.toDouble()
        val placeName = args.placeName
        viewModel.insertPlace(lat, lon, placeName)
    }

    private fun didUserClickOnNewLocation(): Boolean =
        args.placeName.isNotEmpty()
}