package io.github.kPermissions_api

sealed interface PermissionStatus {
    data object Granted : PermissionStatus
    data object Denied : PermissionStatus
    data object DeniedPermanently : PermissionStatus
}

val PermissionStatus.isGranted: Boolean
    get() = this == PermissionStatus.Granted

val PermissionStatus.isDenied: Boolean
    get() = this == PermissionStatus.Denied

val PermissionStatus.isDeniedPermanently: Boolean
    get() = this == PermissionStatus.DeniedPermanently


/**
 * A state object that can be hoisted to control and observe [permission] status changes.
 *
 * In most cases, this will be created via [rememberPermissionState].
 *
 * It's recommended that apps exercise the permissions workflow as described in the
 * [documentation](https://developer.android.com/training/permissions/requesting#workflow_for_requesting_permissions).
 */
interface PermissionState {
    /**
     * The permission to control and observe.
     */
    val permission: Permission

    /**
     * [permission]'s status
     */
    var status: PermissionStatus

    /**
     * Request the [permission] to the user.
     *
     * This should always be triggered from non-composable scope, for example, from a side-effect
     * or a non-composable callback. Otherwise, this will result in an IllegalStateException.
     *
     * This triggers a system dialog that asks the user to grant or revoke the permission.
     * Note that this dialog might not appear on the screen if the user doesn't want to be asked
     * again or has denied the permission multiple times.
     * This behavior varies depending on the Android level API.
     */
    fun launchPermissionRequest()

    /**
     * Open the app settings page.
     *
     * This should always be triggered from non-composable scope, for example, from a side-effect
     * or a non-composable callback. Otherwise, this will result in an IllegalStateException.
     */
    fun openAppSettings()
}