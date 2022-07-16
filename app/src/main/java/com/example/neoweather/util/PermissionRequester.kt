package com.example.neoweather.util

import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission

class PermissionRequester(
    activity: ComponentActivity,
    private val permission: String,
    private val onRationale: () -> Unit = {},
    private val onDenied: () -> Unit = {}
) {
    private var onGranted: () -> Unit = {}

    private val permissionLauncher =
        activity.registerForActivityResult(RequestPermission()) { isGranted ->
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