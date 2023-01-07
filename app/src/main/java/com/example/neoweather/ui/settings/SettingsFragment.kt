package com.example.neoweather.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.neoweather.NeoWeatherApplication
import com.example.neoweather.R
import com.example.neoweather.databinding.FragmentSettingsBinding
import com.example.neoweather.ui.utils.OnUnitCheckedListener

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
                bindTempUnitToggle()
                bindSpeedUnitToggle()
                bindRainUnitToggle()
            }
        }
        setIntervalMenu()

        return binding.root
    }

    private fun bindTempUnitToggle() {
        val selectedButtonId =
            when(viewModel.preferences.value!!.isFahrenheitEnabled) {
                false -> binding.celsiusButton.id
                else -> binding.fahrenheitButton.id
            }
        binding.tempUnitToggle.check(selectedButtonId)

        binding.tempUnitToggle.addOnButtonCheckedListener(
            OnUnitCheckedListener(
                action = { newValue -> viewModel.updateTempUnit(newValue) },
                preferenceState = viewModel.preferences.value!!.isFahrenheitEnabled,
                selectedButtonId = selectedButtonId,
                firstOptionId = binding.celsiusButton.id,
                secondOptionId = binding.fahrenheitButton.id
            )
        )
    }

    private fun bindSpeedUnitToggle() {
        val selectedButtonId =
            when(viewModel.preferences.value!!.isMilesEnabled) {
                false -> binding.kilometersButton.id
                else -> binding.milesButton.id
            }
        binding.speedUnitToggle.check(selectedButtonId)

        binding.speedUnitToggle.addOnButtonCheckedListener(
            OnUnitCheckedListener(
                action = { newValue -> viewModel.updateSpeedUnit(newValue) },
                preferenceState = viewModel.preferences.value!!.isMilesEnabled,
                selectedButtonId = selectedButtonId,
                firstOptionId = binding.kilometersButton.id,
                secondOptionId = binding.milesButton.id
            )
        )
    }

    private fun bindRainUnitToggle() {
        val selectedButtonId =
            when(viewModel.preferences.value!!.isInchesEnabled) {
                false -> binding.millimetersButton.id
                else -> binding.inchesButton.id
            }
        binding.rainUnitToggle.check(selectedButtonId)

        binding.rainUnitToggle.addOnButtonCheckedListener(
            OnUnitCheckedListener(
                action = { newValue -> viewModel.updateRainUnit(newValue) },
                preferenceState = viewModel.preferences.value!!.isInchesEnabled,
                selectedButtonId = selectedButtonId,
                firstOptionId = binding.millimetersButton.id,
                secondOptionId = binding.inchesButton.id
            )
        )
    }

    private fun setIntervalMenu() {
        val items = resources.getStringArray(R.array.interval_options)
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.menu_list_item,
            items
        )
        with(binding.intervalMenu) {
            setAdapter(adapter)
            onItemClickListener = OnItemClickListener { parent, _, position, _ ->
                val item = parent?.getItemAtPosition(position).toString()
                viewModel.updateNotificationsInterval(item)
            }
        }
    }
}

private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}