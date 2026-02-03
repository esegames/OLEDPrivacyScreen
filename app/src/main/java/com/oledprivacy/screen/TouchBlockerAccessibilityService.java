package com.oledprivacy.screen;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;

public class TouchBlockerAccessibilityService extends AccessibilityService {

    private static boolean enabled = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // This service is mainly for improved touch blocking
        // The actual blocking is handled by the overlay view flags
    }

    @Override
    public void onInterrupt() {
        // Called when service is interrupted
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        enabled = true;
        
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_TOUCH_INTERACTION_START |
                         AccessibilityEvent.TYPE_TOUCH_INTERACTION_END;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        info.flags = AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE;
        info.notificationTimeout = 100;
        
        setServiceInfo(info);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        enabled = false;
    }

    public static boolean isEnabled(Context context) {
        String service = context.getPackageName() + "/" + TouchBlockerAccessibilityService.class.getName();
        String enabledServices = Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        );
        
        if (TextUtils.isEmpty(enabledServices)) {
            return false;
        }
        
        TextUtils.SimpleStringSplitter splitter = new TextUtils.SimpleStringSplitter(':');
        splitter.setString(enabledServices);
        
        while (splitter.hasNext()) {
            String componentName = splitter.next();
            if (componentName.equalsIgnoreCase(service)) {
                return true;
            }
        }
        
        return false;
    }
}
