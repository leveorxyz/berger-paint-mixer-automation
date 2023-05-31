package dev.atick.berger.ui.connection.state

import dev.atick.bluetooth.common.models.BtDevice
import dev.atick.core.ui.base.BaseUiState
import dev.atick.core.ui.utils.UiText

data class ConnectionUiState(
    override val loading: Boolean = false,
    override val toastMessage: UiText? = null,
    val pairedDevices: List<BtDevice> = emptyList(),
    val scannedDevices: List<BtDevice> = emptyList(),
    val scanning: Boolean = false,
) : BaseUiState()