package com.example.neoweather.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neoweather.NeoWeatherApplication
import com.example.neoweather.R
import com.example.neoweather.databinding.FragmentSearchBinding
import com.example.neoweather.ui.search.adapter.LocationListener
import com.example.neoweather.ui.search.adapter.SearchListAdapter

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by activityViewModels {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        SearchViewModelFactory(
            (activity.application as NeoWeatherApplication).repository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSearchBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.searchListRecyclerView.adapter =
            SearchListAdapter(LocationListener { location ->
                viewModel.onLocationClicked(location)
                findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
            })
        binding.searchListRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        binding.confirmSearchButton.setOnClickListener {
            if (!binding.searchInputField.text.isNullOrEmpty())
                viewModel.updateLocationList(binding.searchInputField.text.toString())
        }

        return binding.root
    }
}