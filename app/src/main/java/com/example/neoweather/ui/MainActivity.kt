package com.example.neoweather.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.neoweather.NeoWeatherApplication
import com.example.neoweather.R
import com.example.neoweather.remote.geocoding.model.GeoLocation
import com.example.neoweather.ui.utils.PermissionRequester
import com.example.neoweather.ui.home.HomeViewModel
import com.example.neoweather.ui.home.HomeViewModelFactory
import com.example.neoweather.ui.settings.observeOnce
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder

@SuppressLint("MissingPermission")
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private lateinit var settingsIcon: MenuItem

    private lateinit var searchIcon: MenuItem

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            (application as NeoWeatherApplication).repository
        )
    }

    private val coarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION

    @SuppressLint("InlinedApi")
    private val backgroundLocationPermission = Manifest.permission.ACCESS_BACKGROUND_LOCATION

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
            activity = this,
            coarseLocation,
        )
        requestLocationPermission()
    }

    private fun requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            locationPermissionRequester.request {
                if (isBackgroundLocationPermissionEnabled())
                    askForBackgroundLocationPermission { openAppSettings() }
                getLocation()
                viewModel.preferences.observeOnce(this) { preferences ->
                    if (preferences != null)
                        viewModel.enqueueWorkers(this)
                }
            }
    }

    private fun getLocation() {
        if (!isGpsEnabled(this))
            return
        val currentLocation = fusedLocationClient.lastLocation

        currentLocation.addOnSuccessListener { location ->
            viewModel.insertOrUpdatePlace(
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

    private fun isBackgroundLocationPermissionEnabled(): Boolean =
        (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
            checkSelfPermission(backgroundLocationPermission)
            != PackageManager.PERMISSION_GRANTED)

    private fun Context.askForBackgroundLocationPermission(action: () -> Unit) {
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.permanent_location_dialog_title))
            .setMessage(resources.getString(R.string.permanent_location_dialog_message))
            .setNeutralButton(resources.getString(R.string.permanent_location_dialog_button)) { _, _ ->
                action()
            }
            .show()
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

fun Context.openAppSettings() {
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        addCategory(Intent.CATEGORY_DEFAULT)
        data = Uri.parse("package:$packageName")
    }.let(::startActivity)
}

fun isGpsEnabled(context: Context): Boolean {
    val locationManager = context
        .getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager

    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}