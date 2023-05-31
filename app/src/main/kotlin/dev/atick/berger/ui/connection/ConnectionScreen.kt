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

package dev.atick.berger.ui.connection

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.SensorsOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.atick.berger.R
import dev.atick.berger.ui.connection.components.DeviceCard
import dev.atick.core.ui.components.TopBar
import kotlinx.coroutines.launch

@Composable
fun ConnectionScreen(
    connectionViewModel: ConnectionViewModel = viewModel(),
) {
    val connectionUiState by connectionViewModel
        .connectionUiState
        .collectAsStateWithLifecycle()
    val snackBarHost = remember { SnackbarHostState() }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    connectionUiState.toastMessage?.let {
        val errorMessage = it.asString()
        LaunchedEffect(connectionUiState.toastMessage) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    snackBarHost.showSnackbar(errorMessage)
                    connectionViewModel.clearError()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.connection),
                menuItems = {
                    IconButton(onClick = {
                        connectionViewModel.scanForBluetoothDevices()
                    }) {
                        Icon(
                            imageVector = if (connectionUiState.scanning) {
                                Icons.Default.Sensors
                            } else {
                                Icons.Default.SensorsOff
                            },
                            contentDescription = stringResource(id = R.string.scan),
                        )
                    }
                    IconButton(onClick = { connectionViewModel.refreshPairedDevices() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(id = R.string.refresh),
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHost) },
    ) {
        if (connectionUiState.pairedDevices.isEmpty() &&
            connectionUiState.scannedDevices.isEmpty()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier.fillMaxWidth(0.9F).aspectRatio(1.2F),
                    painter = painterResource(id = R.drawable.empty),
                    contentDescription = stringResource(
                        R.string.no_device,
                    ),
                )
                Text(
                    text = stringResource(R.string.no_devices),
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }
                if (connectionUiState.scannedDevices.isNotEmpty()) {
                    item { Text(text = "Scanned Devices", fontSize = 18.sp) }
                    items(connectionUiState.scannedDevices) { device ->
                        DeviceCard(
                            device = device,
                            onClick = { connectionViewModel.connect(device.address) },
                        )
                    }
                }
                if (connectionUiState.pairedDevices.isNotEmpty()) {
                    item { Text(text = "Paired Devices", fontSize = 18.sp) }
                    items(connectionUiState.pairedDevices) { device ->
                        DeviceCard(
                            device = device,
                            onClick = { connectionViewModel.connect(device.address) },
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}
