package com.oledprivacy.screen;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private SeekBar seekActivation;
    private SeekBar seekDeactivation;
    private TextView tvActivationValue;
    private TextView tvDeactivationValue;
    private Spinner spinnerShape;
    private Spinner spinnerColor;
    private Button btnSave;
    
    private SharedPreferences prefs;
    
    private static final String[] SHAPES = {"circle", "square", "triangle", "pentagon", "hexagon", "star"};
    private static final String[] COLORS = {"Gray", "White", "Red", "Green", "Blue", "Yellow", "Cyan", "Magenta"};
    private static final int[] COLOR_VALUES = {
        0xFF808080, // Gray
        0xFFFFFFFF, // White
        0xFFFF0000, // Red
        0xFF00FF00, // Green
        0xFF0000FF, // Blue
        0xFFFFFF00, // Yellow
        0xFF00FFFF, // Cyan
        0xFFFF00FF  // Magenta
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        // Enable back button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        prefs = getSharedPreferences("OLEDPrivacyPrefs", MODE_PRIVATE);
        
        // Initialize views
        seekActivation = findViewById(R.id.seekActivation);
        seekDeactivation = findViewById(R.id.seekDeactivation);
        tvActivationValue = findViewById(R.id.tvActivationValue);
        tvDeactivationValue = findViewById(R.id.tvDeactivationValue);
        spinnerShape = findViewById(R.id.spinnerShape);
        spinnerColor = findViewById(R.id.spinnerColor);
        btnSave = findViewById(R.id.btnSave);
        
        // Set up spinners
        ArrayAdapter<String> shapeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, SHAPES);
        shapeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerShape.setAdapter(shapeAdapter);
        
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, COLORS);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColor.setAdapter(colorAdapter);
        
        // Load current settings
        loadSettings();
        
        // Set up seek bar listeners
        seekActivation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvActivationValue.setText(progress + " ms");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        
        seekDeactivation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvDeactivationValue.setText(progress + " ms");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        
        // Save button
        btnSave.setOnClickListener(v -> saveSettings());
    }

    private void loadSettings() {
        int activationDuration = prefs.getInt("activation_duration", 2000);
        int deactivationDuration = prefs.getInt("deactivation_duration", 2000);
        String shape = prefs.getString("button_shape", "circle");
        int color = prefs.getInt("button_color", 0xFF808080);
        
        seekActivation.setProgress(activationDuration);
        seekDeactivation.setProgress(deactivationDuration);
        tvActivationValue.setText(activationDuration + " ms");
        tvDeactivationValue.setText(deactivationDuration + " ms");
        
        // Set shape spinner
        for (int i = 0; i < SHAPES.length; i++) {
            if (SHAPES[i].equals(shape)) {
                spinnerShape.setSelection(i);
                break;
            }
        }
        
        // Set color spinner
        for (int i = 0; i < COLOR_VALUES.length; i++) {
            if (COLOR_VALUES[i] == color) {
                spinnerColor.setSelection(i);
                break;
            }
        }
    }

    private void saveSettings() {
        int activationDuration = seekActivation.getProgress();
        int deactivationDuration = seekDeactivation.getProgress();
        String shape = SHAPES[spinnerShape.getSelectedItemPosition()];
        int color = COLOR_VALUES[spinnerColor.getSelectedItemPosition()];
        
        prefs.edit()
            .putInt("activation_duration", activationDuration)
            .putInt("deactivation_duration", deactivationDuration)
            .putString("button_shape", shape)
            .putInt("button_color", color)
            .apply();
        
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
