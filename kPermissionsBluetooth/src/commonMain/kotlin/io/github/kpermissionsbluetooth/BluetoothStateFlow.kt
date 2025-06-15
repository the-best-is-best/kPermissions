package io.github.kpermissionsbluetooth

import kotlinx.coroutines.flow.Flow

expect fun bluetoothStateFlow(): Flow<Boolean>
