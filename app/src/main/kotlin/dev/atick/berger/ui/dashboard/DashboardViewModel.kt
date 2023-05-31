/*
 * Copyright 2023 Atick Faisal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.atick.berger.ui.dashboard

import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.atick.berger.ui.dashboard.state.BatchNumber
import dev.atick.berger.ui.dashboard.state.DashboardUiState
import dev.atick.bluetooth.common.data.BluetoothDataSource
import dev.atick.bluetooth.common.manager.BluetoothManager
import dev.atick.core.ui.base.BaseViewModel
import dev.atick.core.ui.utils.UiText
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val bluetoothManager: BluetoothManager,
    private val bluetoothDataSource: BluetoothDataSource,
) : BaseViewModel<DashboardUiState>() {

    private val _dashboardUiState = MutableStateFlow(DashboardUiState())
    val dashboardUiState: StateFlow<DashboardUiState>
        get() = _dashboardUiState.asStateFlow()

    private var seconds = 0.0F
    private var power = 0.0F
    private val powerConsumption = mutableListOf<Entry>()

    private var dataTransmissionJob: Job? = null
    private var disconnectJob: Job? = null

    fun setBatchNumber(batchName: String) {
        val batchNumber = if (batchName == BatchNumber.BATCH_1.value) {
            BatchNumber.BATCH_1
        } else {
            BatchNumber.BATCH_2
        }
        _dashboardUiState.update { it.copy(batchNumber = batchNumber) }
    }

    fun turnOn() {
        _dashboardUiState.update { it.copy(inProgress = true) }
        val data = when (dashboardUiState.value.batchNumber) {
            BatchNumber.BATCH_1 -> "A"
            BatchNumber.BATCH_2 -> "B"
        }
        sendDataToBergerDevice(data)
        trackEnergyConsumption()
    }

    fun turnOff() {
        _dashboardUiState.update { it.copy(inProgress = false) }
        val data = when (dashboardUiState.value.batchNumber) {
            BatchNumber.BATCH_1 -> "X"
            BatchNumber.BATCH_2 -> "Y"
        }
        sendDataToBergerDevice(data)
    }

    private fun trackEnergyConsumption() {
        powerConsumption.clear()
        viewModelScope.launch {
            while (dashboardUiState.value.inProgress) {
                powerConsumption.add(
                    Entry(seconds / 60.0F, power),
                )
                _dashboardUiState.update {
                    it.copy(
                        powerConsumption = LineDataSet(
                            powerConsumption,
                            "Power (W)",
                        ),
                    )
                }
                seconds += 1.0F
                power += 0.01F
                delay(1000L)
            }
        }
    }

    private fun sendDataToBergerDevice(data: String) {
        if (dataTransmissionJob != null) return
        _dashboardUiState.update { it.copy(loading = true) }
        dataTransmissionJob = viewModelScope.launch {
            val result = bluetoothDataSource.sendDataToBluetoothDevice(data)
            if (result.isFailure) {
                val exception = result.exceptionOrNull()
                exception?.localizedMessage?.run {
                    _dashboardUiState.update {
                        it.copy(toastMessage = UiText.DynamicString(this))
                    }
                }
            }
            dataTransmissionJob = null
            _dashboardUiState.update { it.copy(loading = false) }
        }
    }

    fun disconnect() {
        if (disconnectJob != null) return
        _dashboardUiState.update {
            it.copy(
                toastMessage = UiText.DynamicString(
                    "Disconnecting. Please Wait ... ",
                ),
                loading = true,
            )
        }
        disconnectJob = viewModelScope.launch {
            val result = bluetoothManager.closeConnection()
            if (result.isFailure) {
                val exception = result.exceptionOrNull()
                exception?.localizedMessage?.run {
                    _dashboardUiState.update {
                        it.copy(toastMessage = UiText.DynamicString(this))
                    }
                }
            }
            disconnectJob = null
            _dashboardUiState.update { it.copy(loading = false) }
        }
    }

    fun clearError() {
        _dashboardUiState.update { it.copy(toastMessage = null) }
    }
}
