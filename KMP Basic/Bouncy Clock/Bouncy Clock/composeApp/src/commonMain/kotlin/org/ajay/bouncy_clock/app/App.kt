package org.ajay.bouncy_clock.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        GetPermissionOverTiramisu {
            BouncyClock()
        }
    }
}







@Composable
fun GetPermissionOverTiramisu(
    content: @Composable () -> Unit
) {
    val factory = rememberPermissionsControllerFactory()
    val lifecycleOwner = LocalLifecycleOwner.current

    val controller = remember(factory) {
        factory.createPermissionsController()
    }
    BindEffect(controller)
    val viewModel = viewModel {
        PermissionViewModel(controller)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.refreshPermissionState()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    when (viewModel.state.value) {
        PermissionState.Granted -> {
            content()
        }

        else -> {
            viewModel.showPermissionRationaleDialog()
        }
    }
    val screenState = viewModel.getPermissionScreenState()
    if (viewModel.isVisibleRationaleDialog.value) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = screenState.title
                    )
                }
            },
            text = {
                Text(text = screenState.rationale)
            },
            confirmButton = {
                Button(
                    onClick = {
                        when (viewModel.state.value) {

                            PermissionState.Granted -> {
                                viewModel.closePermissionRationaleDialog()
                            }

                            PermissionState.DeniedAlways -> controller.openAppSettings()
                            else -> viewModel.provideOrRequestRecordAudioPermission()
                        }

                    }
                ) {
                    Text(text = screenState.buttonText)
                }
            }
        )
    }
}
