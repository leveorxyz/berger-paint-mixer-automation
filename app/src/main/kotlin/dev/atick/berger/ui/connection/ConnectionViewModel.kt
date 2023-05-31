package dev.atick.berger.ui.connection

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.berger.ui.connection.state.ConnectionUiState
import dev.atick.bluetooth.common.manager.BluetoothManager
import dev.atick.bluetooth.common.utils.BluetoothUtils
import dev.atick.core.ui.base.BaseViewModel
import dev.atick.core.ui.utils.UiText
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectionViewModel @Inject constructor(
    private val bluetoothUtils: BluetoothUtils,
    private val bluetoothManager: BluetoothManager,
) : BaseViewModel<ConnectionUiState>() {

    private val _connectionUiState = MutableStateFlow(ConnectionUiState())
    val connectionUiState: StateFlow<ConnectionUiState>
        get() = _connectionUiState.asStateFlow()

    private var connectionJob: Job? = null

    fun refreshPairedDevices() {
        _connectionUiState.update {
            it.copy(
                toastMessage = UiText.DynamicString(
                    "Refreshing Paired Devices ... "
                )
            )
        }
        bluetoothUtils.getPairedDevices()
            .onEach { pairedDevices ->
                _connectionUiState.update { it.copy(pairedDevices = pairedDevices) }
            }
            .launchIn(viewModelScope)
    }

    fun scanForBluetoothDevices() {
        if (connectionUiState.value.scanning) {
            stopDiscovery()
            return
        }
        _connectionUiState.update {
            it.copy(
                scanning = true,
                toastMessage = UiText.DynamicString(
                    "Scanning for Devices ... "
                )
            )
        }
        bluetoothUtils.getScannedDevices()
            .onEach { scannedDevices ->
                _connectionUiState.update { it.copy(scannedDevices = scannedDevices) }
            }
            .launchIn(viewModelScope)
    }

    fun connect(address: String) {
        if (connectionJob != null) return
        _connectionUiState.update {
            it.copy(
                toastMessage = UiText.DynamicString(
                    "Connecting. Please Wait ... "
                )
            )
        }
        stopDiscovery()
        connectionJob = viewModelScope.launch {
            val result = bluetoothManager.connect(address)
            if (result.isFailure) {
                val exception = result.exceptionOrNull()
                exception?.localizedMessage?.run {
                    _connectionUiState.update {
                        it.copy(toastMessage = UiText.DynamicString(this))
                    }
                }
            }
            connectionJob = null
        }
    }

    private fun stopDiscovery() {
        _connectionUiState.update { it.copy(scanning = false) }
        bluetoothUtils.stopDiscovery()
    }

    fun clearError() {
        _connectionUiState.update { it.copy(toastMessage = null) }
    }
}