# OLED Privacy Screen

An Android app that creates a pure black overlay to save battery on OLED screens while keeping the screen active. Perfect for privacy and power saving.

## Features

- ✅ Pure black (#000000) overlay that turns off OLED pixels
- ✅ Floating button control (draggable when inactive, fixed when active)
- ✅ Customizable long-press duration for activation/deactivation
- ✅ Multiple button shapes (circle, square, triangle, pentagon, hexagon, star)
- ✅ Customizable button colors
- ✅ Full screen mode when active (hides status bar and navigation)
- ✅ Touch blocking via accessibility service
- ✅ Screen stays awake (no auto-sleep)

## How to Build

### Prerequisites
- Android Studio (latest version recommended)
- Android SDK 24 or higher
- Java 8 or higher

### Steps

1. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the `OLEDPrivacyScreen` folder
   - Click "OK"

2. **Wait for Gradle Sync**
   - Android Studio will automatically sync Gradle
   - Wait for the process to complete (may take a few minutes on first run)

3. **Build the APK**
   - Go to `Build` → `Build Bundle(s) / APK(s)` → `Build APK(s)`
   - Wait for the build to complete
   - Click "locate" in the notification to find the APK

4. **Install on Phone**
   - Transfer the APK to your phone
   - Open the APK file on your phone
   - Allow installation from unknown sources if prompted
   - Install the app

## How to Use

### Initial Setup

1. **Open the app**
2. **Grant Permissions**
   - Click "Grant Overlay Permission" and enable it
   - Click "Enable Accessibility Service" and enable "OLED Privacy Screen"
   - Return to the app

3. **Start the Service**
   - Click "Start Overlay Service"
   - You should see a floating button appear on your screen

### Using the Overlay

1. **Activate Black Screen**
   - Long press the floating button (default: 2 seconds)
   - Screen goes completely black
   - Only the button remains visible and responsive

2. **Deactivate**
   - Long press the fixed button again (default: 2 seconds)
   - Screen returns to normal
   - Button becomes draggable again

### Customization

1. **Open Settings**
   - Click the "Settings" button in the main app

2. **Available Settings**
   - **Activation Duration**: How long to hold to activate (500-5000ms)
   - **Deactivation Duration**: How long to hold to deactivate (500-5000ms)
   - **Button Shape**: Choose from 6 shapes
   - **Button Color**: Choose from 8 colors

## Technical Details

### Permissions Required
- `SYSTEM_ALERT_WINDOW` - Draw overlay on other apps
- `FOREGROUND_SERVICE` - Keep service running
- `WAKE_LOCK` - Prevent screen from sleeping
- `BIND_ACCESSIBILITY_SERVICE` - Enhanced touch blocking

### Battery Savings
- Pure black pixels on OLED screens consume no power
- The overlay turns off all pixels except the small button
- However, keeping the screen on still uses some battery
- Net savings depends on your usage pattern

### Android Version Support
- Minimum: Android 7.0 (API 24)
- Target: Android 14 (API 34)
- Tested on: Android 10+

## Troubleshooting

### Button not appearing
- Make sure you granted overlay permission
- Try stopping and starting the service again
- Restart the app

### Touch still going through
- Enable the accessibility service
- Go to Settings → Accessibility → OLED Privacy Screen → Enable

### Screen turning off
- Check if battery optimization is disabled for the app
- The wake lock should prevent this, but some manufacturers override it

### Long press not working
- Adjust the duration in Settings
- Make sure you're holding, not tapping
- Don't move your finger while holding

## Privacy & Security

- ✅ No internet permission required
- ✅ No data collection
- ✅ All settings stored locally
- ✅ Open source code
- ✅ No ads or tracking

## Known Limitations

- Some system dialogs may still appear on top
- Emergency calls and alarms will override the overlay
- Some aggressive battery savers may stop the service
- On some devices, the accessibility service may need to be re-enabled after reboot

## Future Enhancements

- Custom button sizes
- Scheduled activation/deactivation
- Gesture-based unlock patterns
- Widget for quick toggle
- Multiple button positions presets

## License

This project is provided as-is for personal use.

## Credits

Created for privacy-conscious users who want to maximize their OLED battery life.
