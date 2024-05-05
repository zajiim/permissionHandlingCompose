package com.example.permissionhandlingcompose.presentation.home.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun PermissionDialog(
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Ok")
            }
        },
        title = {
            Text(
                text = "Camera and Mic permissions are required",
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Text(
                text = "This app needs access to your camera and mic for proper functioning",
                fontWeight = FontWeight.Normal
            )
        }
    )

}