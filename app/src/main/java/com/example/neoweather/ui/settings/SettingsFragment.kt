package com.example.neoweather.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.neoweather.databinding.FragmentSettingsBinding
import com.example.neoweather.viewmodel.NeoWeatherViewModel
import com.example.neoweather.viewmodel.NeoWeatherViewModelFactory

class SettingsFragment : Fragment() {

    private val viewModel: NeoWeatherViewModel by activityViewModels {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        NeoWeatherViewModelFactory(activity.application)
    }

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        bindTempUnitToggle()

        return binding.root
    }

    private fun bindTempUnitToggle() {
        var currentTempUnit =
            when(viewModel.preferences.value!!.isFahrenheitEnabled) {
                false -> binding.fahrenheitButton.id
                else -> binding.celsiusButton.id
            }
        binding.tempUnitToggle.check(currentTempUnit)

        binding.tempUnitToggle
            .addOnButtonCheckedListener { group, checkedId, isChecked ->
                if (isChecked) {
                    when(checkedId) {
                        binding.fahrenheitButton.id ->
                            viewModel.updateTempUnit(false)
                        else ->
                            viewModel.updateTempUnit(true)
                    }
                } else {
                    if (group.checkedButtonId == View.NO_ID) {
                        currentTempUnit =
                            when(viewModel.preferences.value!!.isFahrenheitEnabled) {
                                false -> binding.fahrenheitButton.id
                                else -> binding.celsiusButton.id
                            }
                        group.check(currentTempUnit)
                    }
                }
            }
    }
}