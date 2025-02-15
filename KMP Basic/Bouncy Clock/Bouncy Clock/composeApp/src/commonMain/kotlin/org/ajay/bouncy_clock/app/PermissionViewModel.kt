package org.ajay.bouncy_clock.app

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import kotlinx.coroutines.launch

class PermissionViewModel(
    private val controller: PermissionsController
) : ViewModel() {
    var state = mutableStateOf(PermissionState.NotDetermined)
        private set

    private var _isVisibleRationaleDialog = mutableStateOf(false)
    val isVisibleRationaleDialog: State<Boolean> = _isVisibleRationaleDialog

    init {
        viewModelScope.launch {
            state.value = controller.getPermissionState(Permission.REMOTE_NOTIFICATION)
        }
    }

    fun provideOrRequestRecordAudioPermission() {
        viewModelScope.launch {
            try {
                controller.providePermission(Permission.REMOTE_NOTIFICATION)
                state.value = PermissionState.Granted
            } catch (e: DeniedAlwaysException) {
                state.value = PermissionState.DeniedAlways
            } catch (e: DeniedException) {
                state.value = PermissionState.Denied
            } catch (e: RequestCanceledException) {
                e.printStackTrace()
            }
        }
    }

    fun refreshPermissionState() {
        viewModelScope.launch {
            state.value = controller.getPermissionState(Permission.REMOTE_NOTIFICATION)
        }
    }

    fun showPermissionRationaleDialog() {
        _isVisibleRationaleDialog.value = true
    }

    fun closePermissionRationaleDialog() {
        _isVisibleRationaleDialog.value = false
    }

    fun getPermissionScreenState(): PermissionScreenState {
        return when (state.value) {
            PermissionState.Granted -> {
                PermissionScreenState(
                    title = "Welcome!",
                    rationale = "Thank you very much! Enjoy the Flip Clock.",
                    buttonText = "Ok"
                )
            }

            PermissionState.DeniedAlways -> {
                PermissionScreenState(
                    title = "Permissions denied",
                    buttonText = "Open Settings",
                    rationale = "Notification permission was permanently denied. Please enable it in app settings."
                )
            }

            PermissionState.Denied -> {
                PermissionScreenState(
                    title = "Notification Permission",
                    rationale = "In order to display count-down through notification, notification permission is needed.\n\nPlease grant the permission to run the app properly.",
                    buttonText = "Grant"
                )

            }

            else -> {
                PermissionScreenState(
                    title = "Notification Permission",
                    buttonText = "Grant",
                    rationale = "This permission is essential to function the app properly.\n\nPlease grant the permission."
                )
            }
        }
    }
}


data class PermissionScreenState(
    val title: String,
    val buttonText: String,
    val rationale: String
)