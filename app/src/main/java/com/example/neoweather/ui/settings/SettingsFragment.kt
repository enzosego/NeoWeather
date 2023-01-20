package com.example.neoweather.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.neoweather.R
import com.example.neoweather.data.local.model.preferences.Preferences
import com.example.neoweather.databinding.FragmentSettingsBinding
import com.example.neoweather.ui.askForBackgroundLocationPermission
import com.example.neoweather.ui.isBackgroundPermissionGranted
import com.example.neoweather.ui.openAppSettings
import com.google.android.material.materialswitch.MaterialSwitch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModel()

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        viewModel.preferences.observeOnce(viewLifecycleOwner) { preferences ->
            if (preferences != null) {
                setSpeedUnitMenu(preferences)
                setRainUnitMenu(preferences)
                setTempUnitMenu(preferences)
                setIntervalMenu(preferences)
            }
        }
        viewModel.currentInterval.observe(viewLifecycleOwner) {
            if (viewModel.hasUserChangedInterval.value == true)
                viewModel.startPeriodicWork()
        }
        viewModel.areNotificationsEnabled.observeOnce(viewLifecycleOwner) {
            if (it != null) {
                binding.notificationsSwitch.isChecked = it
                binding.notificationsSwitch.setUp()
            }
        }
        return binding.root
    }

    private fun MaterialSwitch.setUp() {
        setOnCheckedChangeListener { _, isChecked ->
            if (!isBackgroundPermissionGranted(requireContext()) &&
                    viewModel.preferences.value?.areNotificationsEnabled == false) {
                this.isChecked = false
                with(requireContext()) {
                    askForBackgroundLocationPermission { openAppSettings() }
                }
                return@setOnCheckedChangeListener
            }
            viewModel.toggleNotifications(isChecked, requireContext())
        }
    }

    private fun setTempUnitMenu(preferences: Preferences) {
        val newHint = when (preferences.isFahrenheitEnabled) {
            true -> "Fahrenheit"
            false -> "Celsius"
        }
        binding.tempUnitMenu.applyToMenu(newHint, R.array.temp_unit_options) { item ->
            viewModel.updateTempUnit(item)
        }
    }

    private fun setSpeedUnitMenu(preferences: Preferences) {
        val newHint = when (preferences.isMilesEnabled) {
                true -> "Mph"
                false -> "Kph"
            }
        binding.speedUnitMenu.applyToMenu(newHint, R.array.speed_unit_options) { item ->
            viewModel.updateSpeedUnit(item)
        }
    }

    private fun setRainUnitMenu(preferences: Preferences) {
        val newHint = when (preferences.isInchesEnabled) {
            true -> "Inches"
            false -> "Millimeters"
        }
        binding.rainUnitMenu.applyToMenu(newHint, R.array.rain_unit_options) { item ->
            viewModel.updateRainUnit(item)
        }
    }

    private fun setIntervalMenu(preferences: Preferences) {
        val newHint = when(preferences.notificationsInterval) {
            1L -> "One hour"
            3L -> "Three hours"
            6L -> "Six hours"
            else -> "Twelve hours"
        }
        binding.intervalMenu.applyToMenu(newHint, R.array.interval_options) { item ->
            viewModel.updateNotificationsInterval(item)
        }
    }

    private fun AutoCompleteTextView.applyToMenu(
        newHint: String,
        arrayResource: Int,
        updateDatabase: (item: String) -> Unit = {})
    {
        hint = newHint
        val itemArray = resources.getStringArray(arrayResource)
        setAdapter(
            ArrayAdapter(requireContext(), R.layout.menu_list_item, itemArray)
        )
        onItemClickListener = OnItemClickListener { parent, _, position, _ ->
            val item = parent?.getItemAtPosition(position).toString()
            updateDatabase(item)
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