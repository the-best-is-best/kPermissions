package io.github.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.kpermissions.enum.EnumAppPermission
import io.github.kpermissions.handler.PermissionHandler
import io.github.sample.theme.AppTheme

@Composable
internal fun App() = AppTheme {
    val permission = PermissionHandler()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedButton(
            content = { Text("click test permissions") },

            onClick = {
                permission.requestPermission(EnumAppPermission.LOCATION) {
                    println("permission $it")
                    if (!it) {
                        permission.openAppSettings()
                    }
                }
            }
        )
    }
}
