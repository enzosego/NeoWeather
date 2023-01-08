package com.example.neoweather.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.neoweather.NeoWeatherApplication
import com.example.neoweather.R
import com.example.neoweather.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        SettingsViewModelFactory(
            (activity.application as NeoWeatherApplication).repository
        )
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

        viewModel.preferences.observeOnce(viewLifecycleOwner) { preferences ->
            if (preferences != null) {
                setSpeedUnitMenu(
                    newHint = when (preferences.isMilesEnabled) {
                        true -> "Mph"
                        false -> "Kph"
                    }
                )
                setRainUnitMenu(
                    newHint = when (preferences.isInchesEnabled) {
                        true -> "Inches"
                        false -> "Millimeters"
                    }
                )
                setTempUnitMenu(
                    newHint = when (preferences.isFahrenheitEnabled) {
                        true -> "Fahrenheit"
                        false -> "Celsius"
                    }
                )
                setIntervalMenu(
                    newHint = when(preferences.notificationsInterval) {
                        1L -> "One hour"
                        3L -> "Three hours"
                        6L -> "Six hours"
                        else -> "Twelve hours"
                    }
                )
            }
        }
        viewModel.currentInterval.observe(viewLifecycleOwner) { interval ->
            if (interval != null && viewModel.hasUserChangedInterval.value == true)
                viewModel.startPeriodicWork(requireContext())
        }
        viewModel.areNotificationsEnabled.observeOnce(viewLifecycleOwner) {
            if (it != null) {
                binding.notificationsSwitch.isChecked = it

                binding.notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
                    viewModel.toggleNotifications(isChecked, requireContext())
                }
            }
        }
        return binding.root
    }

    private fun setTempUnitMenu(newHint: String) {
        binding.tempUnitMenu.applyToMenu(newHint, R.array.temp_unit_options) { item ->
            viewModel.updateTempUnit(item)
        }
    }

    private fun setSpeedUnitMenu(newHint: String) {
        binding.speedUnitMenu.applyToMenu(newHint, R.array.speed_unit_options) { item ->
            viewModel.updateSpeedUnit(item)
        }
    }

    private fun setRainUnitMenu(newHint: String) {
        binding.rainUnitMenu.applyToMenu(newHint, R.array.rain_unit_options) { item ->
            viewModel.updateRainUnit(item)
        }
    }

    private fun setIntervalMenu(newHint: String) {
        binding.intervalMenu.applyToMenu(newHint, R.array.interval_options) { item ->
            viewModel.updateNotificationsInterval(item)
        }
    }

    private fun AutoCompleteTextView.applyToMenu(
        newHint: String,
        arrayResource: Int,
        then: (item: String) -> Unit = {})
    {
        hint = newHint
        val itemArray = resources.getStringArray(arrayResource)
        setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.menu_list_item,
                itemArray
            )
        )
        onItemClickListener = OnItemClickListener { parent, _, position, _ ->
            val item = parent?.getItemAtPosition(position).toString()
            then(item)
        }
    }
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}