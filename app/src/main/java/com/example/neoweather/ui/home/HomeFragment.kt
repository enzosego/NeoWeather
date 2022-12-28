package com.example.neoweather.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.neoweather.NeoWeatherApplication
import com.example.neoweather.databinding.FragmentHomeBinding
import com.example.neoweather.ui.home.adapter.HomeTabAdapter

class HomeFragment : Fragment(){

    private val viewModel: HomeViewModel by activityViewModels {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        HomeViewModelFactory(
            (activity.application as NeoWeatherApplication).repository
        )
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
            adapter = HomeTabAdapter(requireActivity())
        }
        viewModel.currentListSize.observe(viewLifecycleOwner) { size ->
            if (size != null && viewModel.previousListSize.value == null)
                viewModel.syncPreviousSize()
        }

        binding.currentItemNum = binding.homeViewPager.currentItem

        return binding.root
    }
}