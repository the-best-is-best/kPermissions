package io.github.kpermissions.handler.permissions

import kotlin.js.Promise

fun requestCalendarPermission(onPermissionResult: (Boolean) -> Unit) {
    val calendarPermission = js("navigator.permissions.query({ name: 'calendar' })")

}

external fun navigatorPermissionsQuery(name: String): Promise<JsAny?>

external interface PermissionStatus {
    val state: String
}