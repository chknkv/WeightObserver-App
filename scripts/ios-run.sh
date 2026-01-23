set +e
IS_PHYSICAL_DEVICE=false
DEVICE_UDID=""
DEVICE_NAME=""
DEVICECTL_NAME=""

BOOTED_SIMULATOR=$(xcrun simctl list devices | grep "Booted" | head -1)

if [ -n "$BOOTED_SIMULATOR" ]; then

    DEVICE_UDID=$(echo "$BOOTED_SIMULATOR" | grep -o '[A-F0-9-]\{36\}' | head -1)
    DEVICE_NAME=$(echo "$BOOTED_SIMULATOR" | sed 's/.*(\(.*\)).*/\1/' | head -1)
    IS_PHYSICAL_DEVICE=false
    echo "📱 Found running simulator: $DEVICE_NAME ($DEVICE_UDID)"
fi

if [ -z "$DEVICE_UDID" ]; then
    PHYSICAL_DEVICE=$(xcrun xctrace list devices 2>/dev/null | grep -i "iphone\|ipad" | grep -v "Simulator" | head -1)
    
    if [ -n "$PHYSICAL_DEVICE" ]; then
        DEVICE_UDID=$(echo "$PHYSICAL_DEVICE" | grep -oE '\([A-F0-9-]{15,}\)' | tail -1 | tr -d '()')
        
        if [ -z "$DEVICE_UDID" ]; then
            DEVICE_UDID=$(echo "$PHYSICAL_DEVICE" | sed 's/.*(\([A-F0-9-]\{15,\}\)).*/\1/' | tail -1)
        fi
        
        DEVICE_NAME=$(echo "$PHYSICAL_DEVICE" | grep -oE 'iPhone|iPad' | head -1)
        IS_PHYSICAL_DEVICE=true
        echo "📱 Found physical device: $PHYSICAL_DEVICE"
        
        if [ -z "$DEVICE_UDID" ]; then
            echo "❌ Could not extract UDID from device info"
            echo "   Full device info: $PHYSICAL_DEVICE"
            echo "   Please check device connection"
            exit 1
        fi
        
        echo "   UDID: $DEVICE_UDID"
        
        if command -v xcrun &> /dev/null && xcrun devicectl list devices --help &> /dev/null 2>&1; then
            DEVICECTL_NAME=$(xcrun devicectl list devices 2>/dev/null | grep -i "iphone\|ipad" | grep -v "Simulator" | awk '{print $1}' | head -1)
            if [ -n "$DEVICECTL_NAME" ]; then
                echo "   Device name for devicectl: $DEVICECTL_NAME"
            fi
        fi
    fi
fi

if [ -z "$DEVICE_UDID" ]; then
    echo "❌ No running simulator or connected device found"
    echo ""
    echo "Checking for devices..."
    echo ""
    
    echo "📱 Running simulators:"
    BOOTED=$(xcrun simctl list devices | grep "Booted" || echo "")
    if [ -z "$BOOTED" ]; then
        echo "  None found"
        echo ""
        echo "  To start a simulator:"
        echo "    ./device-manager-scripts/ios-simulator.sh start"
        echo "    or open Simulator app manually"
    else
        echo "$BOOTED"
    fi
    
    echo ""
    echo "📱 Physical devices:"
    PHYSICAL=$(xcrun xctrace list devices 2>/dev/null | grep -i "iphone\|ipad" | grep -v "Simulator" || echo "")
    if [ -z "$PHYSICAL" ]; then
        echo "  None found"
        echo ""
        echo "  To connect a device:"
        echo "    1. Connect iPhone/iPad via USB"
        echo "    2. Trust this computer on device"
        echo "    3. Unlock device"
    else
        echo "$PHYSICAL"
    fi
    
    exit 1
fi

echo "📱 Using device: $DEVICE_NAME ($DEVICE_UDID)"

if [ "$IS_PHYSICAL_DEVICE" = true ]; then
    SDK="iphoneos"
    ARCH="iosArm64"
    echo "🔨 Building Kotlin framework for physical device (iosArm64)..."
    ./gradlew :shared:linkDebugFrameworkIosArm64
    
    echo "⏭️  Xcode build will be done with device destination"
else
    SDK="iphonesimulator"
    ARCH="iosSimulatorArm64"
    echo "🔨 Building Kotlin framework for simulator (iosSimulatorArm64)..."
    ./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
    
    echo "🔨 Building Xcode project for simulator..."
    BUILD_OUTPUT=$(xcodebuild -project iosApp/iosApp.xcodeproj \
               -scheme iosApp \
               -configuration Debug \
               -sdk "$SDK" \
               -derivedDataPath build/ios \
               build 2>&1)
    
    BUILD_EXIT_CODE=$?
    
    echo "$BUILD_OUTPUT" | tail -20
    
    if [ $BUILD_EXIT_CODE -ne 0 ] || ! echo "$BUILD_OUTPUT" | grep -qi "BUILD SUCCEEDED"; then
        echo "❌ Xcode build failed!"
        echo ""
        echo "Build errors:"
        echo "$BUILD_OUTPUT" | grep -i "error" | head -10
        exit 1
    fi
    
    echo "✅ Xcode build succeeded!"
fi

BUNDLE_ID=$(xcodebuild -project iosApp/iosApp.xcodeproj -scheme iosApp -showBuildSettings 2>/dev/null | grep "PRODUCT_BUNDLE_IDENTIFIER" | head -1 | sed 's/.*= *//' | tr -d ' ')

if [ -z "$BUNDLE_ID" ]; then
    BUNDLE_ID="com.chknkv.weightobserver.WeightObserver-MobileApp"
fi

if [ "$IS_PHYSICAL_DEVICE" = true ]; then
    echo "📦 Building and installing app on physical device..."
    echo "⚠️  Note: Make sure your device is trusted and unlocked"
    echo "⚠️  You may need to enter your device passcode when prompted"
    
    echo "🔍 Verifying device connection..."
    DEVICE_CHECK=$(xcrun xctrace list devices 2>/dev/null | grep "$DEVICE_UDID" || echo "")
    if [ -z "$DEVICE_CHECK" ]; then
        echo "❌ Device $DEVICE_UDID is not available via xctrace"
        echo "   Trying alternative method..."
        
        AVAILABLE_DEVICES=$(xcodebuild -project iosApp/iosApp.xcodeproj -scheme iosApp -showdestinations 2>/dev/null | grep "$DEVICE_UDID" || echo "")
        if [ -z "$AVAILABLE_DEVICES" ]; then
            echo "❌ Device $DEVICE_UDID is not available"
            echo "   Please check:"
            echo "   1. Device is connected via USB"
            echo "   2. Device is unlocked"
            echo "   3. Device trusts this computer (Settings > General > VPN & Device Management)"
            echo "   4. Try disconnecting and reconnecting the device"
            exit 1
        else
            echo "✅ Device found via xcodebuild"
        fi
    else
        echo "✅ Device connection verified"
    fi
    
    echo "🔨 Building for device..."
    DEVICE_BUILD_OUTPUT=$(xcodebuild -project iosApp/iosApp.xcodeproj \
               -scheme iosApp \
               -configuration Debug \
               -sdk "$SDK" \
               -destination "id=$DEVICE_UDID" \
               -derivedDataPath build/ios \
               -allowProvisioningUpdates \
               build 2>&1)
    
    DEVICE_BUILD_EXIT_CODE=$?
    echo "$DEVICE_BUILD_OUTPUT" | tail -30
    
    if [ $DEVICE_BUILD_EXIT_CODE -eq 0 ] && echo "$DEVICE_BUILD_OUTPUT" | grep -qi "BUILD SUCCEEDED"; then
        echo "✅ Build succeeded!"
        
        APP_PATH="build/ios/Build/Products/Debug-iphoneos/WeightObserver-MobileApp.app"
        
        if [ ! -d "$APP_PATH" ]; then
            FOUND_APP=$(find build/ios -name "*.app" -type d | head -1)
            if [ -n "$FOUND_APP" ]; then
                APP_PATH="$FOUND_APP"
                echo "✅ Found app bundle at: $APP_PATH"
            else
                echo "⚠️  App bundle not found, but build succeeded"
                echo "   App may have been installed directly to device"
            fi
        else
            echo "✅ App bundle found at: $APP_PATH"
        fi
        
        echo "📦 Installing app on device..."
        if [ -d "$APP_PATH" ]; then
            INSTALLED=false
            
            if command -v xcrun &> /dev/null && xcrun devicectl device install app --help &> /dev/null 2>&1; then
                echo "   Trying devicectl..."
                INSTALL_OUTPUT=$(xcrun devicectl device install app --device "$DEVICE_UDID" "$APP_PATH" 2>&1)
                INSTALL_EXIT=$?
                
                if [ $INSTALL_EXIT -ne 0 ] && [ -n "$DEVICECTL_NAME" ]; then
                    echo "   Trying with device name: $DEVICECTL_NAME"
                    INSTALL_OUTPUT=$(xcrun devicectl device install app --device "$DEVICECTL_NAME" "$APP_PATH" 2>&1)
                    INSTALL_EXIT=$?
                fi
                
                if [ $INSTALL_EXIT -eq 0 ]; then
                    echo "✅ App installed via devicectl"
                    INSTALLED=true
                else
                    echo "   devicectl result: $INSTALL_OUTPUT"
                fi
            fi
            
            if [ "$INSTALLED" = false ] && command -v ios-deploy &> /dev/null; then
                echo "   Trying ios-deploy..."
                ios-deploy --bundle "$APP_PATH" --id "$DEVICE_UDID" 2>&1 | head -10
                if [ $? -eq 0 ]; then
                    echo "✅ App installed via ios-deploy"
                    INSTALLED=true
                fi
            fi
            
            if [ "$INSTALLED" = false ]; then
                echo "ℹ️  Direct installation methods didn't work"
                echo "   App may have been installed during xcodebuild build"
                echo "   Or you may need to install via Xcode manually"
            fi
        else
            echo "⚠️  App bundle not found at expected path"
            echo "   Checking if app was installed during build..."             
            if command -v xcrun &> /dev/null && xcrun devicectl device process launch --help &> /dev/null 2>&1; then
                echo "   Trying to verify installation by attempting launch..."
            fi
        fi
        
        echo "🚀 Launching app on physical device..."
        LAUNCHED=false
        
        if command -v xcrun &> /dev/null && xcrun devicectl device process launch --help &> /dev/null 2>&1; then
            echo "   Trying devicectl launch..."
            LAUNCH_OUTPUT=$(xcrun devicectl device process launch --device "$DEVICE_UDID" "$BUNDLE_ID" 2>&1)
            LAUNCH_EXIT_CODE=$?
            
            if [ $LAUNCH_EXIT_CODE -ne 0 ] && [ -n "$DEVICECTL_NAME" ]; then
                echo "   Trying with device name: $DEVICECTL_NAME"
                LAUNCH_OUTPUT=$(xcrun devicectl device process launch --device "$DEVICECTL_NAME" "$BUNDLE_ID" 2>&1)
                LAUNCH_EXIT_CODE=$?
            fi
            
            if [ $LAUNCH_EXIT_CODE -eq 0 ]; then
                echo "✅ App launched successfully via devicectl!"
                LAUNCHED=true
            else
                if echo "$LAUNCH_OUTPUT" | grep -qi "already running\|process.*running"; then
                    echo "ℹ️  App appears to be already running"
                    LAUNCHED=true
                else
                    echo "   devicectl launch result: $LAUNCH_OUTPUT"
                fi
            fi
        fi
        
        if [ "$LAUNCHED" = false ] && command -v ios-deploy &> /dev/null; then
            echo "   Trying ios-deploy launch..."
            ios-deploy --id "$DEVICE_UDID" --bundle "$APP_PATH" --justlaunch 2>&1 | head -5
            if [ $? -eq 0 ]; then
                echo "✅ App launched via ios-deploy!"
                LAUNCHED=true
            fi
        fi
        
        if [ "$LAUNCHED" = false ]; then
            echo "⚠️  Could not launch app automatically"
            echo "   App should be installed on your device"
            echo "   Please launch it manually from the home screen"
            echo ""
            echo "   Or use Xcode to launch:"
            echo "   open iosApp/iosApp.xcodeproj"
            echo "   Then press Cmd+R in Xcode"
        fi
    else
        echo "❌ Build failed. Check errors above."
        echo ""
        echo "Common issues:"
        echo "  1. Device not trusted: Settings > General > VPN & Device Management"
        echo "  2. Device locked - unlock it"
        echo "  3. Code signing issues - check Xcode settings"
        echo "  4. Try installing via Xcode: open iosApp/iosApp.xcodeproj and press Cmd+R"
        exit 1
    fi
    echo "✅ Done! Check your device for the app."
else
    APP_PATH="build/ios/Build/Products/Debug-iphonesimulator/WeightObserver-MobileApp.app"
    
    if [ ! -d "$APP_PATH" ]; then
        echo "❌ App not found at $APP_PATH"
        echo "Trying to find app..."
        FOUND_APP=$(find build/ios -name "*.app" -type d | head -1)
        if [ -n "$FOUND_APP" ]; then
            APP_PATH="$FOUND_APP"
            echo "Found app at: $APP_PATH"
        else
            echo "❌ Could not find .app file"
            exit 1
        fi
    fi
    
    echo "📦 Installing app on simulator..."
    
    if ! xcrun simctl list devices | grep "$DEVICE_UDID" | grep -q "Booted"; then
        echo "⚠️  Simulator $DEVICE_NAME is no longer running"
        echo "   Trying to boot it..."
        xcrun simctl boot "$DEVICE_UDID" 2>/dev/null || {
            echo "❌ Failed to boot simulator"
            exit 1
        }
        sleep 2
    fi
    
    INSTALL_RESULT=$(xcrun simctl install "$DEVICE_UDID" "$APP_PATH" 2>&1)
    INSTALL_EXIT_CODE=$?
    
    if [ $INSTALL_EXIT_CODE -eq 0 ]; then
        echo "✅ App installed successfully"
    else
        echo "⚠️  Installation warning: $INSTALL_RESULT"
    fi
    
    echo "🚀 Launching app with bundle ID: $BUNDLE_ID"
    LAUNCH_RESULT=$(xcrun simctl launch "$DEVICE_UDID" "$BUNDLE_ID" 2>&1)
    LAUNCH_EXIT_CODE=$?
    
    if [ $LAUNCH_EXIT_CODE -eq 0 ]; then
        PROCESS_ID=$(echo "$LAUNCH_RESULT" | grep -o '[0-9]*' | head -1)
        if [ -n "$PROCESS_ID" ]; then
            echo "✅ App launched successfully!"
            echo "   Process ID: $PROCESS_ID"
        else
            echo "✅ Launch command succeeded"
        fi
    else
        if echo "$LAUNCH_RESULT" | grep -qi "already running\|booted"; then
            echo "ℹ️  App may already be running"
        else
            echo "⚠️  Launch result: $LAUNCH_RESULT"
            echo "   Trying to launch again..."
            sleep 1
            xcrun simctl launch "$DEVICE_UDID" "$BUNDLE_ID" 2>&1 | head -3
        fi
    fi
    
    echo "✅ Done! App should be running on simulator."
fi

