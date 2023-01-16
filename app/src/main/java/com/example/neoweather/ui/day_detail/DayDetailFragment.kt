package com.example.neoweather.ui.day_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neoweather.databinding.FragmentDayDetailBinding
import com.example.neoweather.ui.day_detail.adapter.DayDetailHourListAdapter
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class DayDetailFragment : Fragment() {

    private val viewModel: DayDetailViewModel by activityViewModel()

    private val args: DayDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDayDetailBinding.inflate(inflater)

        binding.lifecycleOwner = this

        viewModel.getDayDetail(args.dayNum, args.position)

        binding.hourListRecyclerview.adapter =
            DayDetailHourListAdapter(viewModel.dayByHours.value!!.hourList)
        binding.hourListRecyclerview.layoutManager =
            LinearLayoutManager(requireContext())

        return binding.root
    }
}
