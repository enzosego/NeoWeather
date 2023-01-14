package com.example.neoweather.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.example.neoweather.R
import com.example.neoweather.databinding.FragmentSearchBinding
import com.example.neoweather.ui.search.adapter.LocationListener
import com.example.neoweather.ui.search.adapter.SearchListAdapter
import com.example.neoweather.ui.utils.ApiStatus
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSearchBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        val searchListAdapter =
            SearchListAdapter(LocationListener { location ->
                viewModel.onLocationClicked(location)
                findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
            })
        searchListAdapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onChanged() {
                binding.searchListRecyclerView.scrollToPosition(0)
            }
        })
        binding.searchListRecyclerView.adapter = searchListAdapter

        binding.searchListRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        /*
        binding.confirmSearchButton.setOnClickListener {
            if (!binding.searchInputField.text.isNullOrEmpty())
                viewModel.updateLocationList(binding.searchInputField.text.toString())
        }
         */
        binding.searchInputField.addTextChangedListener {
            if (!binding.searchInputField.text.isNullOrEmpty())
                viewModel.updateLocationList(binding.searchInputField.text.toString())
        }
        viewModel.status.observe(viewLifecycleOwner) { status ->
            if (status == ApiStatus.ERROR && binding.searchInputField.text.isNullOrEmpty())
                viewModel.cleanApiStatus()
        }
        return binding.root
    }
}