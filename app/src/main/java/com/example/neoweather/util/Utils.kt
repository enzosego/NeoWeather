package com.example.neoweather.util

import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.Instant
import java.util.*

object Utils {
    const val TAG = "DEBUG"

    enum class NeoWeatherApiStatus { LOADING, DONE, ERROR }

    const val accessToken: String =
        "pk.fdd7a17016391ba2f9c3084f67679b44"

    fun getTimeDiffInMinutes(time: Long): Long =
        ((Date.from(Instant.now()).time - time) / 1000) / 60

    class PermissionRequester(
        activity: ComponentActivity,
        private val permission: String,
        private val onRationale: () -> Unit = {},
        private val onDenied: () -> Unit = {}
    ) {
        private var onGranted: () -> Unit = {}

        private val permissionLauncher =
            activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                when {
                    isGranted -> onGranted()
                    activity.shouldShowRequestPermissionRationale(permission) ->
                        onRationale()
                    else -> onDenied()
                }
            }

        fun request(body: () -> Unit) {
            onGranted = body
            permissionLauncher.launch(permission)
        }
    }

    class ApiBuilder(baseUrl: String) {
        private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(baseUrl)
            .build()
    }
}