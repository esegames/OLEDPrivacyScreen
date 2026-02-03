# App Workflow Diagram

## User Journey

```
┌─────────────────────────────────────────────────────────────────┐
│                        USER OPENS APP                            │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                      MainActivity                                │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  Check Permissions:                                       │  │
│  │  • Overlay Permission (SYSTEM_ALERT_WINDOW)              │  │
│  │  • Accessibility Service Enabled                          │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                  │
│  IF MISSING:                                                     │
│  → Show "Grant Overlay Permission" button                       │
│  → Show "Enable Accessibility Service" button                   │
│                                                                  │
│  IF ALL GRANTED:                                                 │
│  → Enable "Start Overlay Service" button                        │
│  → Show "Settings" button                                        │
└────────────────────────┬────────────────────────────────────────┘
                         │
         USER CLICKS "START OVERLAY SERVICE"
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                    OverlayService Starts                         │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  1. Create Foreground Notification                        │  │
│  │  2. Acquire Wake Lock (screen stays on)                   │  │
│  │  3. Create FloatingButtonView                             │  │
│  │  4. Add button to WindowManager                           │  │
│  └──────────────────────────────────────────────────────────┘  │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│            FLOATING BUTTON APPEARS ON SCREEN                     │
│                   (Draggable State)                              │
│                                                                  │
│                      ┌─────┐                                     │
│                      │  ●  │  ← Gray circle (or other shape)    │
│                      └─────┘                                     │
│                                                                  │
│  User can:                                                       │
│  • Drag button anywhere on screen                               │
│  • Long press to activate                                        │
└────────────────────────┬────────────────────────────────────────┘
                         │
       USER LONG PRESSES BUTTON (2+ seconds)
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                  OVERLAY ACTIVATES                               │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  1. Create BlackOverlayView (pure black #000000)          │  │
│  │  2. Add overlay to WindowManager (full screen)            │  │
│  │  3. Set button to FIXED mode (non-draggable)              │  │
│  │  4. Bring button to front (above black overlay)           │  │
│  │  5. Enter full screen mode (hide status/nav bars)         │  │
│  └──────────────────────────────────────────────────────────┘  │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                    SCREEN IS NOW BLACK                           │
│  ┌────────────────────────────────────────────────────────┐    │
│  │                                                         │    │
│  │              PURE BLACK SCREEN                          │    │
│  │         (All OLED pixels turned off)                    │    │
│  │                                                         │    │
│  │                                                         │    │
│  │                    ┌─────┐                              │    │
│  │                    │  ●  │  ← Button visible & fixed    │    │
│  │                    └─────┘                              │    │
│  │                                                         │    │
│  │              (Touches blocked everywhere                │    │
│  │               except on the button)                     │    │
│  │                                                         │    │
│  └────────────────────────────────────────────────────────┘    │
└────────────────────────┬────────────────────────────────────────┘
                         │
       USER LONG PRESSES BUTTON AGAIN (2+ seconds)
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                 OVERLAY DEACTIVATES                              │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  1. Remove BlackOverlayView from WindowManager            │  │
│  │  2. Set button to DRAGGABLE mode                          │  │
│  │  3. Exit full screen mode (show status/nav bars)          │  │
│  └──────────────────────────────────────────────────────────┘  │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│              BACK TO NORMAL - BUTTON DRAGGABLE                   │
│                                                                  │
│  User sees normal screen with apps underneath                   │
│  Button can be dragged again                                     │
│  Can long press to activate again anytime                        │
└─────────────────────────────────────────────────────────────────┘
```

## Settings Customization Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                   USER CLICKS "SETTINGS"                         │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                   SettingsActivity Opens                         │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  Load current settings from SharedPreferences:            │  │
│  │  • activation_duration (default 2000ms)                   │  │
│  │  • deactivation_duration (default 2000ms)                 │  │
│  │  • button_color (default gray)                            │  │
│  │  • button_shape (default circle)                          │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                  │
│  Display Controls:                                               │
│  ┌─────────────────────────────────────────┐                   │
│  │ Activation Duration: [━━●━━━━] 2000 ms  │                   │
│  │ Deactivation Duration: [━━●━━━] 2000 ms │                   │
│  │ Shape: [Circle ▼] Square Triangle ...   │                   │
│  │ Color: [Gray ▼] White Red Blue ...      │                   │
│  │                                          │                   │
│  │            [ SAVE SETTINGS ]             │                   │
│  └─────────────────────────────────────────┘                   │
└────────────────────────┬────────────────────────────────────────┘
                         │
              USER ADJUSTS AND CLICKS SAVE
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                  Save to SharedPreferences                       │
│  Settings will be loaded next time button is created            │
└─────────────────────────────────────────────────────────────────┘
```

## Touch Blocking via Accessibility Service

```
┌─────────────────────────────────────────────────────────────────┐
│         TouchBlockerAccessibilityService (if enabled)            │
│                                                                  │
│  Runs in background, monitors all touch events                  │
│                                                                  │
│  When overlay is ACTIVE:                                         │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  Touch Event Detected                                     │  │
│  │         │                                                  │  │
│  │         ▼                                                  │  │
│  │  Is touch on FloatingButton?                              │  │
│  │         │                                                  │  │
│  │    ┌────┴────┐                                            │  │
│  │   YES       NO                                            │  │
│  │    │         │                                            │  │
│  │    ▼         ▼                                            │  │
│  │  Allow   Block touch                                      │  │
│  │  (button (prevent apps                                    │  │
│  │  works)  below from                                       │  │
│  │          receiving)                                       │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

## Component Communication

```
MainActivity ←→ SharedPreferences ←→ SettingsActivity
     │                                      │
     │ starts/stops                         │ saves preferences
     │                                      │
     ▼                                      ▼
OverlayService ←─────────────────→ FloatingButtonView
     │                reads settings             │
     │                                          │
     │ manages                                  │ triggers
     │                                          │
     ▼                                          ▼
BlackOverlayView                         Long Press Detection
     │                                          │
     │                                          │
     └──────────── Both added to ──────────────┘
                  WindowManager
                       │
                       ▼
              User sees overlay on screen
```

## Service Lifecycle

```
App Start → OverlayService.onCreate()
              │
              ├─→ Create WindowManager reference
              ├─→ Acquire WakeLock
              ├─→ Create FloatingButtonView
              └─→ Add button to screen

Service Running → OverlayService.onStartCommand()
                    │
                    └─→ Start foreground with notification

User Interaction → toggleOverlay()
                     │
                     ├─→ If inactive: activateOverlay()
                     │     │
                     │     ├─→ Create BlackOverlayView
                     │     ├─→ Add to WindowManager
                     │     ├─→ Set button to fixed
                     │     └─→ Full screen mode ON
                     │
                     └─→ If active: deactivateOverlay()
                           │
                           ├─→ Remove BlackOverlayView
                           ├─→ Set button to draggable
                           └─→ Full screen mode OFF

App Close/Stop → OverlayService.onDestroy()
                   │
                   ├─→ Remove all views from WindowManager
                   ├─→ Release WakeLock
                   └─→ Service stops
```

## Permission Flow

```
                    ┌─────────────────────┐
                    │  User Opens App     │
                    └──────────┬──────────┘
                               │
                ┌──────────────┴──────────────┐
                │                              │
                ▼                              ▼
    ┌────────────────────┐         ┌──────────────────────┐
    │ Check Overlay      │         │ Check Accessibility  │
    │ Permission         │         │ Service              │
    └─────────┬──────────┘         └──────────┬───────────┘
              │                                │
       ┌──────┴──────┐                 ┌──────┴──────┐
      NO            YES               NO            YES
       │              │                 │              │
       ▼              │                 ▼              │
  Show "Grant"        │           Show "Enable"        │
  button              │           button               │
       │              │                 │              │
  User clicks         │           User clicks          │
       │              │                 │              │
       ▼              │                 ▼              │
  Open Settings       │           Open Accessibility   │
  (overlay)           │           Settings             │
       │              │                 │              │
  User enables        │           User enables         │
       │              │                 │              │
       └──────┬───────┘                 └──────┬───────┘
              │                                 │
              └────────────┬────────────────────┘
                           │
                           ▼
                  ┌──────────────────┐
                  │ All Permissions  │
                  │ Granted          │
                  └────────┬─────────┘
                           │
                           ▼
                  ┌──────────────────┐
                  │ Enable "Start    │
                  │ Service" button  │
                  └──────────────────┘
```

## Battery & Power Management

```
┌─────────────────────────────────────────────────────────────────┐
│                     Power States                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  INACTIVE STATE (Button visible, draggable):                    │
│  ┌────────────────────────────────────────────────────────┐    │
│  │ • Screen ON (WakeLock active)                          │    │
│  │ • Most pixels displaying normal apps                   │    │
│  │ • Small button consuming minimal power                 │    │
│  │ • Battery drain: Normal + WakeLock overhead            │    │
│  └────────────────────────────────────────────────────────┘    │
│                                                                  │
│  ACTIVE STATE (Black overlay):                                  │
│  ┌────────────────────────────────────────────────────────┐    │
│  │ • Screen ON (WakeLock active)                          │    │
│  │ • 99.9% of pixels turned OFF (black = no light)        │    │
│  │ • Only button pixels emitting light                    │    │
│  │ • Battery drain: WakeLock + minimal OLED power         │    │
│  │ • NET SAVINGS: Significant on OLED screens             │    │
│  └────────────────────────────────────────────────────────┘    │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

This workflow shows exactly how all the components work together!
