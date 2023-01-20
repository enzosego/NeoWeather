package com.example.neoweather.ui.days

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.neoweather.databinding.FragmentDaysBinding
import com.example.neoweather.ui.days.adapter.DayDetailTabAdapter
import com.example.neoweather.ui.days.day_detail.DayDetailViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class DaysFragment : Fragment() {

    private val args: DaysFragmentArgs by navArgs()

    private val viewModel: DayDetailViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDaysBinding.inflate(inflater)

        viewModel.getDays(args.placeId)

        binding.daysViewPager.apply {
            adapter = DayDetailTabAdapter(requireActivity(), args.placeId)
            setCurrentItem(args.position, false)
        }


        return binding.root
    }
}