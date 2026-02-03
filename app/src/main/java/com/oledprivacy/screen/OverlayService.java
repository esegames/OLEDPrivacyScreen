package com.oledprivacy.screen;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.Gravity;
import android.view.WindowManager;

import androidx.core.app.NotificationCompat;

public class OverlayService extends Service {

    private static final int NOTIFICATION_ID = 1001;
    private static boolean running = false;
    
    private WindowManager windowManager;
    private FloatingButtonView floatingButton;
    private BlackOverlayView blackOverlay;
    private PowerManager.WakeLock wakeLock;
    
    private boolean overlayActive = false;

    @Override
    public void onCreate() {
        super.onCreate();
        running = true;
        
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        
        // Acquire wake lock to keep screen on
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                "OLEDPrivacy::WakeLock"
        );
        wakeLock.acquire();
        
        createFloatingButton();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(NOTIFICATION_ID, createNotification());
        return START_STICKY;
    }

    private void createFloatingButton() {
        // Layout parameters for floating button
        int layoutFlag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutFlag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutFlag = WindowManager.LayoutParams.TYPE_PHONE;
        }
        
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutFlag,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT
        );
        
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 100;
        params.y = 100;
        
        // Create floating button
        floatingButton = new FloatingButtonView(this, this::toggleOverlay);
        floatingButton.setLayoutParams(params);
        
        windowManager.addView(floatingButton, params);
    }

    private void toggleOverlay() {
        if (!overlayActive) {
            activateOverlay();
        } else {
            deactivateOverlay();
        }
    }

    private void activateOverlay() {
        overlayActive = true;
        
        // Create black overlay
        int layoutFlag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutFlag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutFlag = WindowManager.LayoutParams.TYPE_PHONE;
        }
        
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                layoutFlag,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT
        );
        
        params.gravity = Gravity.TOP | Gravity.START;
        
        blackOverlay = new BlackOverlayView(this);
        windowManager.addView(blackOverlay, params);
        
        // Make floating button fixed (non-draggable) and bring to front
        floatingButton.setDraggable(false);
        floatingButton.bringToFront();
        
        // Remove and re-add button to ensure it's on top
        windowManager.removeView(floatingButton);
        
        WindowManager.LayoutParams buttonParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutFlag,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT
        );
        
        buttonParams.gravity = Gravity.TOP | Gravity.START;
        buttonParams.x = floatingButton.getCurrentX();
        buttonParams.y = floatingButton.getCurrentY();
        
        windowManager.addView(floatingButton, buttonParams);
    }

    private void deactivateOverlay() {
        overlayActive = false;
        
        // Remove black overlay
        if (blackOverlay != null) {
            windowManager.removeView(blackOverlay);
            blackOverlay = null;
        }
        
        // Make floating button draggable again
        floatingButton.setDraggable(true);
    }

    private Notification createNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
        );

        return new NotificationCompat.Builder(this, "overlay_service_channel")
                .setContentTitle("OLED Privacy Screen")
                .setContentText("Overlay service is running")
                .setSmallIcon(android.R.drawable.ic_lock_lock)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;
        
        if (floatingButton != null) {
            windowManager.removeView(floatingButton);
        }
        
        if (blackOverlay != null) {
            windowManager.removeView(blackOverlay);
        }
        
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    public static boolean isRunning() {
        return running;
    }
}
