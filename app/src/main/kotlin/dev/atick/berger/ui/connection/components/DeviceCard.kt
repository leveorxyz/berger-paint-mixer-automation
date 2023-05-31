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
import dev.atick.bluetooth.common.models.BtDeviceType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceCard(
    modifier: Modifier = Modifier,
    device: BtDevice,
    onClick: (BtDevice) -> Unit,
) {
    Card(
        modifier = modifier,
        onClick = { onClick(device) },
    ) {
        Row(
            modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = when (device.type) {
                        BtDeviceType.AUDIO_VIDEO -> Icons.Default.BluetoothAudio
                        BtDeviceType.COMPUTER -> Icons.Default.Computer
                        BtDeviceType.HEALTH -> Icons.Default.DirectionsRun
                        BtDeviceType.IMAGING -> Icons.Default.Image
                        BtDeviceType.NETWORKING -> Icons.Default.Cloud
                        BtDeviceType.PERIPHERAL -> Icons.Default.Image
                        BtDeviceType.PHONE -> Icons.Default.KeyboardAlt
                        BtDeviceType.TOY -> Icons.Default.Toys
                        BtDeviceType.UNCATEGORIZED -> Icons.Default.Bluetooth
                        BtDeviceType.WEARABLE -> Icons.Default.Watch
                        else -> Icons.Outlined.ViewInAr
                    },
                    contentDescription = stringResource(id = R.string.device_type),
                    modifier = Modifier.size(32.dp),
                )

                Spacer(modifier = Modifier.width(24.dp))

                Column {
                    Text(
                        text = device.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = device.address,
                        fontSize = 14.sp,
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.NavigateNext,
                contentDescription = stringResource(id = R.string.connect),
            )
        }
    }
}
