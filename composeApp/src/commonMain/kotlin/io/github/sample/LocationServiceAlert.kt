package io.github.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.kPermissions_api.PermissionState
import io.github.kpermissionlocationwheninuseext.openPrivacySettings
import io.github.kpermissionslocationWhenInUse.LocationInUsePermission
import kotlinx.coroutines.launch

@Composable
fun LocationServiceAlert(state: PermissionState) {
    val scope = rememberCoroutineScope()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        // Orange tone
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Location Service is Disabled",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Please enable location services to proceed.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    scope.launch {
                        try {
                            LocationInUsePermission.openPrivacySettings()
                        } catch (_: UnsupportedOperationException) {
                            state.launchPermissionRequest()
                        }
                    }
                }
            ) {
                Text("Open Location Settings")
            }
        }
    }
}
