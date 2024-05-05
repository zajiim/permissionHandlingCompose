package com.example.permissionhandlingcompose.presentation.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.permissionhandlingcompose.presentation.home.components.PermissionDialog


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = remember { HomeViewModel() },
) {
    val context = LocalContext.current as Activity
    val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )
    val showDialog = viewModel.showDialog.collectAsState().value
    val launchAppSettings = viewModel.launchAppSettings.collectAsState().value
    val permissionsGranted = viewModel.permissionsGranted.collectAsState().value
    val permissionsResultActivityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { result ->
            val allPermissionsGranted = permissions.all { permission ->
                result[permission] ?: false
            }
            if (allPermissionsGranted) {
                viewModel.checkPermission(context, permissions)
            } else {
                permissions.forEach { permission ->
                    if (result[permission] == false) {
                        if (!context.shouldShowRequestPermissionRationale(permission)) {
                            viewModel.updateLaunchAppSettings(true)
                        }
                        viewModel.updateShowDialog(true)
                    }
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.checkPermission(context, permissions)
    }


    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                if (!permissionsGranted) {
                    permissionsResultActivityLauncher.launch(permissions)
                }
//                permissions.forEach { permission ->
//                    val isGranted = context.checkSelfPermission(permission) ==
//                            PackageManager.PERMISSION_GRANTED
//                    if (!isGranted) {
//                        if (context.shouldShowRequestPermissionRationale(permission)) {
//                            viewModel.updateShowDialog(true)
//                        } else {
//                            permissionsResultActivityLauncher.launch(permissions)
//                        }
//                    }
//                }
            }
        ) {
            Text(text = if (permissionsGranted) "Permissions Granted" else "Request Permission")
        }
    }

    if (showDialog) {
        PermissionDialog(
            onDismiss = { viewModel.updateShowDialog(false) },
            onConfirm = {
                viewModel.updateShowDialog(false)
                if (launchAppSettings) {
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts(
                            "package",
                            context.packageName,
                            null
                        )
                    ).also {
                        context.startActivity(it)
                    }
                    viewModel.updateLaunchAppSettings(false)
                } else {
                    permissionsResultActivityLauncher.launch(permissions)
                }
            }
        )
    }
}