package dev.atick.berger.ui.connection.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothAudio
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardAlt
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Toys
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material.icons.outlined.ViewInAr
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.atick.berger.R
import dev.atick.bluetooth.common.models.BtDevice
import dev.atick.bluetooth.common.models.BtDeviceType.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceCard(
    modifier: Modifier = Modifier,
    device: BtDevice,
    onClick: (BtDevice) -> Unit
) {
    Card(
        modifier = modifier,
        onClick = { onClick(device) }
    ) {
        Row(
            modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = when (device.type) {
                        AUDIO_VIDEO -> Icons.Default.BluetoothAudio
                        COMPUTER -> Icons.Default.Computer
                        HEALTH -> Icons.Default.DirectionsRun
                        IMAGING -> Icons.Default.Image
                        NETWORKING -> Icons.Default.Cloud
                        PERIPHERAL -> Icons.Default.Image
                        PHONE -> Icons.Default.KeyboardAlt
                        TOY -> Icons.Default.Toys
                        UNCATEGORIZED -> Icons.Default.Bluetooth
                        WEARABLE -> Icons.Default.Watch
                        else -> Icons.Outlined.ViewInAr
                    },
                    contentDescription = stringResource(id = R.string.device_type),
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.width(24.dp))

                Column {
                    Text(
                        text = device.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = device.address,
                        fontSize = 14.sp
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.NavigateNext,
                contentDescription = stringResource(id = R.string.connect)
            )
        }
    }
}