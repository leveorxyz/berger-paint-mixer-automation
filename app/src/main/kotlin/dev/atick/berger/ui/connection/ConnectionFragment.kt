package dev.atick.berger.ui.connection

import androidx.compose.runtime.Composable
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.bluetooth.common.manager.BluetoothManager
import dev.atick.core.extensions.collectWithLifecycle
import dev.atick.core.ui.base.BaseFragment
import javax.inject.Inject

@AndroidEntryPoint
class ConnectionFragment : BaseFragment() {

    @Inject
    lateinit var bluetoothManager: BluetoothManager

    override fun observeStates() {
        collectWithLifecycle(bluetoothManager.getConnectedDeviceState()) { device ->
            if (device?.connected == true) navigateToDashboardFragment()
        }
    }

    @Composable
    override fun ComposeUi() {
        ConnectionScreen()
    }

    private fun navigateToDashboardFragment() {
        findNavController().navigate(
            ConnectionFragmentDirections.actionConnectionFragmentToDashboardFragment()
        )
    }
}