package com.example.neoweather.ui.days

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.neoweather.databinding.FragmentDaysBinding
import com.example.neoweather.ui.days.adapter.DayDetailTabAdapter

class DaysFragment : Fragment() {

    private val args: DaysFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDaysBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.daysViewPager.apply {
            adapter = DayDetailTabAdapter(requireActivity(), args.placeId)
            setCurrentItem(args.position, false)
        }


        return binding.root
    }
}