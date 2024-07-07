package io.github.kpermissions.handler.permission

import java.util.Calendar

fun requestCalendarPermission(onPermissionResult: (Boolean) -> Unit) {
    val calendar = Calendar.getInstance()
    calendar.toInstant()
    calendar.add(10, 20)
    onPermissionResult(true)
}