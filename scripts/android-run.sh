#!/bin/bash

set -e

ANDROID_HOME="${ANDROID_HOME:-$HOME/Library/Android/sdk}"
ADB="$ANDROID_HOME/platform-tools/adb"

if [ ! -f "$ADB" ]; then
    echo "❌ ADB not found at $ADB"
    echo "Please set ANDROID_HOME environment variable or install Android SDK"
    exit 1
fi

DEVICES=$("$ADB" devices | grep -v "List" | grep "device$" | awk '{print $1}')

if [ -z "$DEVICES" ]; then
    echo "❌ No Android device or emulator found"
    echo ""
    echo "Please either:"
    echo "  1. Start an emulator from Android Studio"
    echo "  2. Connect a physical device via USB with USB debugging enabled"
    echo ""
    echo "Checking devices..."
    "$ADB" devices -l
    exit 1
fi

DEVICE_ID=$(echo "$DEVICES" | head -1)
DEVICE_COUNT=$(echo "$DEVICES" | wc -l | tr -d ' ')

if [ "$DEVICE_COUNT" -gt 1 ]; then
    echo "⚠️  Multiple devices found, using first one: $DEVICE_ID"
    echo "All devices:"
    "$ADB" devices -l
    echo ""
else
    DEVICE_INFO=$("$ADB" devices -l | grep "$DEVICE_ID" | sed 's/.*model:\([^ ]*\).*/\1/' || echo "device")
    echo "📱 Using device: $DEVICE_ID ($DEVICE_INFO)"
fi

echo "🔨 Building Android app..."
./gradlew :WeightObserver-MobileApp:assembleDebug

echo "📱 Installing app on device/emulator..."
./gradlew :WeightObserver-MobileApp:installDebug

echo "🚀 Launching app..."
"$ADB" -s "$DEVICE_ID" shell am start -n com.chknkv.weightobserver/.WeightObserverMainActivity

echo "✅ Done! App should be running on your device/emulator."

