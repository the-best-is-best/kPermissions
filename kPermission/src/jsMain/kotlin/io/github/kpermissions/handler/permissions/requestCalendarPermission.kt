package io.github.kpermissions.handler.permissions

fun requestCalendarPermission(onPermissionResult: (Boolean) -> Unit) {
    val calendarPermission = js("navigator.permissions.query({ name: 'calendar' })")
    calendarPermission.then(
        { result ->
            if (result.state == "granted") {
                onPermissionResult(true)
            } else {
                onPermissionResult(false)
            }
        },
        { error ->
            console.error("Error requesting calendar permission: $error")
            onPermissionResult(false)
        }
    )
}