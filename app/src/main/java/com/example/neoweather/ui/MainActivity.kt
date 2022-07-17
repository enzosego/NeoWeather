package com.example.neoweather.ui

import android.Manifest
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.neoweather.R
import com.example.neoweather.util.PermissionRequester
import com.example.neoweather.viewmodel.NeoWeatherViewModel
import com.example.neoweather.viewmodel.NeoWeatherViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private lateinit var settingsIcon: MenuItem

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

    private fun refreshData() {
        if (!isGpsEnabled())
            viewModel.refreshDataFromRepository(mapOf())

        val currentLocation = fusedLocationClient.lastLocation

        currentLocation.addOnSuccessListener { location ->
            viewModel.refreshDataFromRepository(
                mapOf(
                    "latitude" to location.latitude,
                    "longitude" to location.longitude
                )
            )
        }
    }

    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            locationPermissionRequester.request {
                refreshData()
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

        navController.addOnDestinationChangedListener { _, destination, _ ->
            settingsIcon.isVisible =
                when(destination.id) {
                    R.id.homeFragment -> true
                    else -> false
                }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings_icon)
            navController.navigate(R.id.action_homeFragment_to_settingsFragment)
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean =
        navController.navigateUp() || super.onSupportNavigateUp()
}