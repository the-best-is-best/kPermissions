package io.github.kpermissions.enum

enum class EnumAppPermission {
    // Common for Android, iOS, JavaScript, and WebAssembly
    CALENDAR,
    CONTACTS,
    LOCATION,
    LOCATION_ALWAYS,
    LOCATION_WHEN_IN_USE,
    STORAGE,
    CAMERA,
    MICROPHONE,
    NOTIFICATION,
//
//    // Android specific
//    /**
//     * Android only: manage external storage
//     */
//    MANAGE_EXTERNAL_STORAGE,

    /**
     * Android only: access phone (telephony)
     */
    PHONE,

    /**
     * Android only: access sensors (e.g., accelerometer, gyroscope)
     */
    SENSORS,

    /**
     * Android only: access SMS (text messaging)
     */
    SMS,

    // iOS specific
    /**
     * iOS only: manage app tracking transparency
     */
    APP_TRACKING_TRANSPARENCY,

    /**
     * iOS only: access Bluetooth
     */
    BLUETOOTH,

    /**
     * iOS only: access motion sensors (e.g., accelerometer, gyroscope)
     */
    MOTION,

    /**
     * iOS  and android 13
     */
    PHOTOS,


    /**
     * iOS only: access reminders (e.g., calendar events)
     */
    REMINDERS,

    /**
     * iOS only: access speech recognition
     */
    SPEECH

}