package com.example.permissionhandlingcompose.presentation.home

import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {
    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    private val _launchAppSettings = MutableStateFlow(false)
    val launchAppSettings = _launchAppSettings.asStateFlow()

    private val _permissionsGranted = MutableStateFlow(false)
    val permissionsGranted = _permissionsGranted.asStateFlow()

    fun updateShowDialog(show: Boolean) {
        _showDialog.update { show }
    }

    fun updateLaunchAppSettings(launch: Boolean) {
        _launchAppSettings.update { launch }
    }

    fun checkPermission(context: Context, permissions: Array<String>) {
        val allGranted = permissions.all { permission ->
            context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
        }
        _permissionsGranted.value = allGranted
    }
}