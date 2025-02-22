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

import androidx.compose.runtime.Composable
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.bluetooth.common.manager.BluetoothManager
import dev.atick.core.extensions.collectWithLifecycle
import dev.atick.core.ui.base.BaseFragment
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : BaseFragment() {

    @Inject
    lateinit var bluetoothManager: BluetoothManager

    override fun observeStates() {
        collectWithLifecycle(bluetoothManager.getConnectedDeviceState()) { device ->
            if (device?.connected == false) navigateToConnectionFragment()
        }
    }

    @Composable
    override fun ComposeUi() {
        DashboardScreen()
    }

    private fun navigateToConnectionFragment() {
        findNavController().navigate(
            DashboardFragmentDirections.actionDashboardFragmentToConnectionFragment(),
        )
    }
}
