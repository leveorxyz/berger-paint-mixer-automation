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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.atick.berger.R
import dev.atick.berger.ui.dashboard.components.DropdownMenu
import dev.atick.berger.ui.dashboard.components.LinePlot
import dev.atick.berger.ui.dashboard.state.BatchNumber
import dev.atick.core.ui.components.LoadingButton
import dev.atick.core.ui.components.TopBar
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(
    dashboardViewModel: DashboardViewModel = viewModel(),
) {
    val dashboardUiState by dashboardViewModel
        .dashboardUiState
        .collectAsStateWithLifecycle()
    val snackBarHost = remember { SnackbarHostState() }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    dashboardUiState.toastMessage?.let {
        val errorMessage = it.asString()
        LaunchedEffect(dashboardUiState.toastMessage) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    snackBarHost.showSnackbar(errorMessage)
                    dashboardViewModel.clearError()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.dashboard),
                menuItems = {
                    IconButton(onClick = {
                        dashboardViewModel.disconnect()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(id = R.string.close),
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHost) },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            if (dashboardUiState.inProgress) {
                Text(text = "Power Consumption")
                LinePlot(
                    dataset = dashboardUiState.powerConsumption,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                )
                Text(
                    text = "Time (Minutes)",
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            DropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                label = "Batch Number",
                items = enumValues<BatchNumber>().map { it.value },
                selectedItem = dashboardUiState.batchNumber.value,
                onSelect = dashboardViewModel::setBatchNumber,
            )
            Spacer(modifier = Modifier.height(16.dp))
            LoadingButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                loading = false,
                onClick = { dashboardViewModel.turnOn() },
            ) {
                Text(text = "TURN ON", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            LoadingButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                loading = false,
                onClick = { dashboardViewModel.turnOff() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error,
                ),
            ) {
                Text(text = "TURN OFF", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.weight(1F))

            LoadingButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                loading = dashboardUiState.loading,
                onClick = { dashboardViewModel.disconnect() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ),
            ) {
                Text(text = "DISCONNECT", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
