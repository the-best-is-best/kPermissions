package io.github.kpermissions.enum

enum class EnumAppPermission {
    // Common for Android, iOS, JavaScript, and WebAssembly
    CALENDAR_READ,
    CALENDAR_WRITE,
    CONTACTS_WRITE,
    CONTACTS_READ,
    LOCATION,
    LOCATION_ALWAYS,
    LOCATION_WHEN_IN_USE,
    CAMERA,
    MICROPHONE,
    NOTIFICATION,
    BLUETOOTH,

    /**
     * Android only
     */
    PHONE,
    WRITE_STORAGE,
    READ_STORAGE,
    PHOTO,
    VIDEO,


    // iOS specific
    /**
     * iOS only: manage app tracking transparency
     */
    APP_TRACKING_TRANSPARENCY,
    GALLERY,


}