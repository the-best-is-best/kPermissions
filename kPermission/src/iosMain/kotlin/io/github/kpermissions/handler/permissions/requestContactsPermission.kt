package io.github.kpermissions.handler.permissions

import platform.Contacts.CNAuthorizationStatusAuthorized
import platform.Contacts.CNAuthorizationStatusNotDetermined
import platform.Contacts.CNContactStore
import platform.Contacts.CNEntityType

fun requestContactsPermission(onPermissionResult: (Boolean) -> Unit) {
    val contactStore = CNContactStore
    contactStore.initialize()
    when (CNContactStore.authorizationStatusForEntityType(CNEntityType.CNEntityTypeContacts)) {
        CNAuthorizationStatusAuthorized -> {
            onPermissionResult(true)
        }

        CNAuthorizationStatusNotDetermined -> {
            CNContactStore().requestAccessForEntityType(CNEntityType.CNEntityTypeContacts) { granted, _ ->

            onPermissionResult(granted)
            }
        }

        else -> {
            onPermissionResult(false)
        }
    }
}
