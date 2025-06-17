#!/bin/bash

modules=(
    ":kPermissionsApi"
    ":kPermissionsCMP"
    ":kPermissionsCore"

    ":kPermissionsCamera"
    ":kPermissionsStorage"
    ":kPermissionsGallery"
    ":kPermissionsReadAudio"
    ":kPermissionsReadVideo"
    ":kpermissionsLocationWhenInUse"
    ":kpermissionsLocationAlways"
    ":kPermissionsNotification"
    ":kPermissionsBluetooth"

    ":kpermissionsCMPLocationAlways"
    ":kPermissionCMPLocationWhenInUse"
    ":kPermissionsCMPBluetooth"

    ":kpermissionsLocationChecker"
)


for module in "${modules[@]}"
do
    echo "🔄 Publishing $module..."
    ./gradlew "$module:publishAndReleaseToMavenCentral" --no-configuration-cache || {
        echo "❌ Failed to publish $module"
        exit 1
    }
done

echo "✅ All modules published!"
