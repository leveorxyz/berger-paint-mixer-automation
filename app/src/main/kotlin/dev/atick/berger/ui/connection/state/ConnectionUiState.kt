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
