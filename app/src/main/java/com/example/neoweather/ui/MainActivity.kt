package com.example.neoweather.ui

import android.Manifest
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.neoweather.R
import com.example.neoweather.remote.geocoding.GeoLocation
import com.example.neoweather.util.Utils.PermissionRequester
import com.example.neoweather.util.Utils.TAG
import com.example.neoweather.viewmodel.NeoWeatherViewModel
import com.example.neoweather.viewmodel.NeoWeatherViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private lateinit var settingsIcon: MenuItem

    private lateinit var searchIcon: MenuItem

    private val viewModel: NeoWeatherViewModel by viewModels {
        NeoWeatherViewModelFactory(this.application)
    }

    private val coarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION

    private lateinit var locationPermissionRequester: PermissionRequester

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolBar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolBar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)

        fusedLocationClient = LocationServices
            .getFusedLocationProviderClient(this)

        locationPermissionRequester = PermissionRequester(
            this,
            coarseLocation,
        )
        requestLocationPermission()
    }

    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            locationPermissionRequester.request {
                refreshData()
            }
    }

    private fun refreshData() {
        if (!isGpsEnabled()) {
            Log.d(TAG, "Initiating preferences!")
            viewModel.refreshDataFromRepository(null)
            return
        }
        val currentLocation = fusedLocationClient.lastLocation

        currentLocation.addOnSuccessListener { location ->
            viewModel.refreshDataFromRepository(
                GeoLocation(
                    name = "",
                    latitude = location.latitude,
                    longitude = location.longitude,
                    country = "",
                    countryCode = "",
                    timezone = ""
                )
            )
        }
    }

    private fun isGpsEnabled(): Boolean {
        val locationManager = this
            .getSystemService(LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        settingsIcon = menu.findItem(R.id.settings_icon)
        searchIcon = menu.findItem(R.id.search_icon)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            setIconVisibility(settingsIcon, destination)
            setIconVisibility(searchIcon, destination)
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun setIconVisibility(icon: MenuItem, destination: NavDestination) {
        icon.isVisible =
            when(destination.id) {
                R.id.homeFragment -> true
                else -> false
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search_icon ->
                navController.navigate(R.id.action_homeFragment_to_searchFragment)
            R.id.settings_icon ->
                navController.navigate(R.id.action_homeFragment_to_settingsFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp() || super.onSupportNavigateUp()
}