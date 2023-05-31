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
            DashboardFragmentDirections.actionDashboardFragmentToConnectionFragment()
        )
    }
}