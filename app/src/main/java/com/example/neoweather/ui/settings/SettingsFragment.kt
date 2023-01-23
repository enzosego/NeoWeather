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
import com.example.neoweather.data.local.model.preferences.data.DataPreferences
import com.example.neoweather.data.local.model.preferences.units.UnitsPreferences
import com.example.neoweather.databinding.FragmentSettingsBinding
import com.example.neoweather.ui.askForBackgroundLocationPermission
import com.example.neoweather.ui.isBackgroundPermissionGranted
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

        viewModel.unitsPreferences.observeOnce(viewLifecycleOwner) { preferences ->
            if (preferences != null) {
                setSpeedUnitMenu(preferences)
                setRainUnitMenu(preferences)
                setTempUnitMenu(preferences)
            }
        }
        viewModel.dataPreferences.observeOnce(viewLifecycleOwner) { preferences ->
            if (preferences != null) {
                setIntervalMenu(preferences)
                binding.updateInBackgroundSwitch.isChecked = preferences.updateInBackground
                binding.updateInBackgroundSwitch.setOnCheckedChangeListener { _, isChecked ->
                    viewModel.toggleBackgroundUpdates(isChecked)
                }
            }
        }
        viewModel.areNotificationsEnabled.observeOnce(viewLifecycleOwner) {
            if (it != null) {
                binding.notificationsSwitch.isChecked = it
                binding.notificationsSwitch.setListener()
            }
        }
        return binding.root
    }

    private fun MaterialSwitch.setListener() {
        setOnCheckedChangeListener { _ , isChecked ->
            if (viewModel.isPreferredLocationGps() && isChecked
                && !isBackgroundPermissionGranted(requireContext())
            ) {
                this.isChecked = false
                val messageResource = R.string.permanent_location_toggle_message
                requireContext().askForBackgroundLocationPermission(messageResource)
                return@setOnCheckedChangeListener
            }
            viewModel.toggleNotifications(isChecked)
        }
    }

    private fun setTempUnitMenu(unitsPreferences: UnitsPreferences) {
        val newHint = when (unitsPreferences.isFahrenheitEnabled) {
            true -> "Fahrenheit"
            false -> "Celsius"
        }
        binding.tempUnitMenu.setup(newHint, R.array.temp_unit_options) { item ->
            viewModel.updateTempUnit(item)
        }
    }

    private fun setSpeedUnitMenu(unitsPreferences: UnitsPreferences) {
        val newHint = when (unitsPreferences.isMilesEnabled) {
                true -> "Mph"
                false -> "Kph"
            }
        binding.speedUnitMenu.setup(newHint, R.array.speed_unit_options) { item ->
            viewModel.updateSpeedUnit(item)
        }
    }

    private fun setRainUnitMenu(unitsPreferences: UnitsPreferences) {
        val newHint = when (unitsPreferences.isInchesEnabled) {
            true -> "Inches"
            false -> "Millimeters"
        }
        binding.rainUnitMenu.setup(newHint, R.array.rain_unit_options) { item ->
            viewModel.updateRainUnit(item)
        }
    }

    private fun setIntervalMenu(dataPreferences: DataPreferences) {
        val newHint = when(dataPreferences.updateInterval) {
            1L -> "One hour"
            3L -> "Three hours"
            6L -> "Six hours"
            else -> "Twelve hours"
        }
        binding.intervalMenu.setup(newHint, R.array.interval_options) { item ->
            viewModel.setDatabaseUpdateInterval(item)
        }
    }

    private fun AutoCompleteTextView.setup(
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