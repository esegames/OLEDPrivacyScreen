package com.oledprivacy.screen;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int OVERLAY_PERMISSION_REQUEST_CODE = 1001;
    private static final int ACCESSIBILITY_PERMISSION_REQUEST_CODE = 1002;
    
    private Button btnStartService;
    private Button btnStopService;
    private Button btnSettings;
    private Button btnRequestOverlay;
    private Button btnRequestAccessibility;
    private TextView tvStatus;
    
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        prefs = getSharedPreferences("OLEDPrivacyPrefs", MODE_PRIVATE);
        
        // Initialize default settings if first run
        if (!prefs.contains("activation_duration")) {
            prefs.edit()
                .putInt("activation_duration", 2000)
                .putInt("deactivation_duration", 2000)
                .putInt("button_color", 0xFF808080) // Gray
                .putString("button_shape", "circle")
                .apply();
        }
        
        createNotificationChannel();
        
        // Initialize views
        btnStartService = findViewById(R.id.btnStartService);
        btnStopService = findViewById(R.id.btnStopService);
        btnSettings = findViewById(R.id.btnSettings);
        btnRequestOverlay = findViewById(R.id.btnRequestOverlay);
        btnRequestAccessibility = findViewById(R.id.btnRequestAccessibility);
        tvStatus = findViewById(R.id.tvStatus);
        
        // Set up click listeners
        btnStartService.setOnClickListener(v -> startOverlayService());
        btnStopService.setOnClickListener(v -> stopOverlayService());
        btnSettings.setOnClickListener(v -> openSettings());
        btnRequestOverlay.setOnClickListener(v -> requestOverlayPermission());
        btnRequestAccessibility.setOnClickListener(v -> requestAccessibilityPermission());
        
        updateUI();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }
    
    private void updateUI() {
        boolean hasOverlayPermission = checkOverlayPermission();
        boolean hasAccessibilityPermission = checkAccessibilityPermission();
        boolean serviceRunning = OverlayService.isRunning();
        
        // Update status text
        StringBuilder status = new StringBuilder();
        status.append("Overlay Permission: ").append(hasOverlayPermission ? "✓ Granted" : "✗ Not Granted").append("\n");
        status.append("Accessibility Permission: ").append(hasAccessibilityPermission ? "✓ Granted" : "✗ Not Granted").append("\n");
        status.append("Service Status: ").append(serviceRunning ? "Running" : "Stopped");
        tvStatus.setText(status.toString());
        
        // Update button states
        btnRequestOverlay.setEnabled(!hasOverlayPermission);
        btnRequestAccessibility.setEnabled(!hasAccessibilityPermission);
        btnStartService.setEnabled(hasOverlayPermission && !serviceRunning);
        btnStopService.setEnabled(serviceRunning);
    }
    
    private boolean checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(this);
        }
        return true;
    }
    
    private boolean checkAccessibilityPermission() {
        return TouchBlockerAccessibilityService.isEnabled(this);
    }
    
    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);
        }
    }
    
    private void requestAccessibilityPermission() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        Toast.makeText(this, "Please enable 'OLED Privacy Screen' accessibility service", Toast.LENGTH_LONG).show();
        startActivityForResult(intent, ACCESSIBILITY_PERMISSION_REQUEST_CODE);
    }
    
    private void startOverlayService() {
        if (!checkOverlayPermission()) {
            Toast.makeText(this, "Overlay permission required!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Intent serviceIntent = new Intent(this, OverlayService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
        
        Toast.makeText(this, "Service started. Look for the floating button!", Toast.LENGTH_SHORT).show();
        updateUI();
    }
    
    private void stopOverlayService() {
        Intent serviceIntent = new Intent(this, OverlayService.class);
        stopService(serviceIntent);
        Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show();
        updateUI();
    }
    
    private void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "overlay_service_channel",
                    "OLED Privacy Overlay",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Keeps the overlay service running");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateUI();
    }
}
