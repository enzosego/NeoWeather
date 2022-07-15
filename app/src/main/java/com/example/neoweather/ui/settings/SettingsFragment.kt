package com.example.neoweather.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.neoweather.databinding.FragmentSettingsBinding
import com.example.neoweather.viewmodel.NeoWeatherViewModel
import com.example.neoweather.viewmodel.NeoWeatherViewModelFactory

class SettingsFragment : Fragment() {

    private val viewModel: NeoWeatherViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(
            this,
            NeoWeatherViewModelFactory(activity.application)
        )[NeoWeatherViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSettingsBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        return binding.root
    }
}