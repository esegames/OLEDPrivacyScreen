# Quick Start Guide

## Installation Steps

1. **Open Android Studio**
   - Download and install Android Studio if you don't have it
   - Get it from: https://developer.android.com/studio

2. **Open the Project**
   - Launch Android Studio
   - Click "Open" or "Open an Existing Project"
   - Navigate to the `OLEDPrivacyScreen` folder
   - Click "OK"

3. **Wait for Setup**
   - First time will take 5-10 minutes
   - Android Studio will download dependencies
   - Let Gradle sync complete

4. **Build the APK**
   - Click `Build` in the menu bar
   - Select `Build Bundle(s) / APK(s)`
   - Click `Build APK(s)`
   - Wait for "Build successful" message

5. **Find Your APK**
   - Click "locate" in the notification
   - Or go to: `app/build/outputs/apk/debug/`
   - File will be named: `app-debug.apk`

6. **Install on Your Phone**
   - Transfer `app-debug.apk` to your phone
   - Open it on your phone
   - Tap "Install"
   - If blocked, enable "Install from unknown sources" in settings

## First Time Setup (On Phone)

1. Open the app
2. Click "Grant Overlay Permission" → Enable
3. Click "Enable Accessibility Service" → Enable
4. Return to app
5. Click "Start Overlay Service"
6. You'll see a gray circle appear
7. Long press it for 2 seconds → Screen goes black!
8. Long press again → Back to normal

## Customization

Click "Settings" to change:
- How long to hold (activation/deactivation)
- Button shape (circle, square, triangle, etc.)
- Button color (gray, white, red, etc.)

## Tips

- Move the button by dragging it (when not active)
- It stays in place when screen is black
- Screen won't turn off automatically
- Works on top of any app

## Troubleshooting

**Button disappeared?**
- Stop and start the service again

**Touch going through black screen?**
- Enable the accessibility service

**Build errors in Android Studio?**
- File → Invalidate Caches → Invalidate and Restart
- Tools → SDK Manager → Make sure Android SDK 34 is installed

**Can't install APK?**
- Settings → Security → Enable "Unknown sources"
- Or Settings → Apps → Special access → Install unknown apps → Enable for your file manager

## Need Help?

Check the full README.md for detailed information and troubleshooting.
