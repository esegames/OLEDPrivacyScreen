# Project Structure Explained

## Overview
This is a complete Android Studio project for the OLED Privacy Screen app.

## Folder Structure

```
OLEDPrivacyScreen/
│
├── app/                                    # Main application module
│   ├── src/
│   │   └── main/
│   │       ├── java/com/oledprivacy/screen/    # Java source code
│   │       │   ├── MainActivity.java            # Main app screen
│   │       │   ├── SettingsActivity.java        # Settings screen
│   │       │   ├── OverlayService.java          # Background service managing overlay
│   │       │   ├── FloatingButtonView.java      # The draggable/fixed button
│   │       │   ├── BlackOverlayView.java        # The black screen overlay
│   │       │   └── TouchBlockerAccessibilityService.java  # Touch blocking
│   │       │
│   │       ├── res/                        # Resources
│   │       │   ├── layout/                 # UI layouts
│   │       │   │   ├── activity_main.xml        # Main screen layout
│   │       │   │   └── activity_settings.xml    # Settings screen layout
│   │       │   ├── values/                 # Values
│   │       │   │   ├── strings.xml             # Text strings
│   │       │   │   └── themes.xml              # App theme colors
│   │       │   └── xml/                    # XML configs
│   │       │       └── accessibility_service_config.xml
│   │       │
│   │       └── AndroidManifest.xml         # App configuration & permissions
│   │
│   ├── build.gradle                        # App-level build configuration
│   └── proguard-rules.pro                  # Code optimization rules
│
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties       # Gradle wrapper config
│
├── build.gradle                            # Project-level build configuration
├── settings.gradle                         # Project settings
├── gradle.properties                       # Gradle properties
│
├── README.md                               # Full documentation
└── QUICK_START.md                         # Quick setup guide
```

## Key Files Explained

### Java Source Files (app/src/main/java/com/oledprivacy/screen/)

**MainActivity.java** (160 lines)
- Entry point of the app
- Handles permission requests
- Start/stop service buttons
- Status display

**OverlayService.java** (150 lines)
- Foreground service that runs continuously
- Manages floating button and black overlay
- Handles wake lock to keep screen on
- Toggles between active/inactive states

**FloatingButtonView.java** (230 lines)
- Custom view for the floating button
- Handles dragging when inactive
- Detects long press gestures
- Draws different shapes (circle, square, triangle, etc.)
- Changes color based on settings

**BlackOverlayView.java** (25 lines)
- Simple view that draws pure black (#000000)
- Covers entire screen when activated

**SettingsActivity.java** (140 lines)
- Settings screen UI
- Adjusts activation/deactivation timers
- Selects button shape and color
- Saves preferences

**TouchBlockerAccessibilityService.java** (60 lines)
- Accessibility service for enhanced touch blocking
- Prevents touches from reaching apps below overlay
- Checks if service is enabled

### Layout Files (app/src/main/res/layout/)

**activity_main.xml**
- Main screen UI
- Buttons for permissions and service control
- Status text display
- Instructions

**activity_settings.xml**
- Settings screen UI
- Seek bars for timers
- Spinners for shape and color selection
- Save button

### Configuration Files

**AndroidManifest.xml**
- Declares all permissions needed
- Registers activities and services
- Configures accessibility service

**build.gradle (app level)**
- App dependencies (AndroidX, Material Design)
- SDK versions (min: 24, target: 34)
- Build settings

**build.gradle (project level)**
- Android Gradle plugin version
- Repository configurations

## Code Flow

### When App Starts:
1. MainActivity opens
2. Checks if permissions are granted
3. Shows status and enables/disables buttons accordingly

### When Service Starts:
1. User clicks "Start Overlay Service"
2. OverlayService starts as foreground service
3. FloatingButtonView is added to screen
4. Wake lock acquired (screen stays on)

### When User Long Presses Button:
1. FloatingButtonView detects long press
2. Calls OverlayService.toggleOverlay()
3. If inactive → activate:
   - BlackOverlayView added to screen
   - Button becomes fixed (non-draggable)
   - Full screen mode enabled
4. If active → deactivate:
   - BlackOverlayView removed
   - Button becomes draggable
   - Exit full screen mode

### Accessibility Service:
- Runs independently when enabled
- Provides enhanced touch blocking
- Works system-wide

## Important Design Decisions

### Why Foreground Service?
- Prevents Android from killing the app
- Allows continuous operation
- Required for overlay to persist

### Why Wake Lock?
- Keeps screen on at all times
- Essential for the app's purpose
- Without it, screen would auto-sleep

### Why Accessibility Service?
- Improves touch blocking reliability
- Some touches can bypass window flags
- Accessibility service intercepts at system level

### Why Pure Black (#000000)?
- OLED pixels completely turn off
- Maximum battery savings
- True privacy (nothing visible)

### Why Two Different Long Press Durations?
- Flexibility for user preference
- Activation might need different timing than deactivation
- Prevents accidental toggles

## Building Process

When you build this in Android Studio:

1. **Gradle reads** all .gradle files
2. **Compiles** all .java files to bytecode
3. **Processes** all .xml files
4. **Packages** everything into an APK
5. **Signs** the APK (debug or release)
6. **Outputs** the APK to build/outputs/apk/

## Permissions Explained

### SYSTEM_ALERT_WINDOW
- Allows drawing over other apps
- Essential for overlay functionality
- User must grant manually in settings

### FOREGROUND_SERVICE
- Allows service to run continuously
- Shows persistent notification
- Prevents system from killing service

### WAKE_LOCK
- Keeps screen from turning off
- Part of the app's core functionality
- Declared in manifest, granted automatically

### BIND_ACCESSIBILITY_SERVICE
- Allows touch interception
- Enhanced privacy/security
- User must enable in accessibility settings

## Minimum Requirements

- **Android Version**: 7.0+ (API 24+)
- **Target Version**: 14 (API 34)
- **Java**: 8+
- **Gradle**: 8.0
- **Android Gradle Plugin**: 8.1.0

## File Sizes (Approximate)

- Total project: ~50 KB (source only)
- Built APK: ~2-3 MB
- Installed size: ~5-6 MB

## Next Steps After Building

1. Transfer APK to phone
2. Install the app
3. Grant overlay permission
4. Enable accessibility service
5. Start the service
6. Enjoy OLED power savings!
