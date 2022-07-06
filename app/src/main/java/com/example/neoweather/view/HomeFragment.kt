package com.example.neoweather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.neoweather.databinding.FragmentHomeBinding
import com.example.neoweather.viewmodel.NeoWeatherViewModel

class HomeFragment : Fragment() {

    private val viewModel: NeoWeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = this.viewModel

        return binding.root
    }
}