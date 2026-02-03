package com.oledprivacy.screen;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class FloatingButtonView extends View {

    private static final int BUTTON_SIZE = 150; // Size in pixels
    
    private Paint paint;
    private String shape;
    private int color;
    private boolean isDraggable = true;
    
    private float initialX, initialY;
    private float initialTouchX, initialTouchY;
    private int currentX, currentY;
    
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    
    private OnLongPressListener longPressListener;
    private Handler longPressHandler;
    private Runnable longPressRunnable;
    private boolean isLongPressing = false;
    private int longPressDuration;

    public interface OnLongPressListener {
        void onLongPress();
    }

    public FloatingButtonView(Context context, OnLongPressListener listener) {
        super(context);
        this.longPressListener = listener;
        this.windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        
        // Load preferences
        SharedPreferences prefs = context.getSharedPreferences("OLEDPrivacyPrefs", Context.MODE_PRIVATE);
        color = prefs.getInt("button_color", 0xFF808080);
        shape = prefs.getString("button_shape", "circle");
        longPressDuration = prefs.getInt("activation_duration", 2000);
        
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        
        longPressHandler = new Handler();
        longPressRunnable = () -> {
            if (isLongPressing && longPressListener != null) {
                longPressListener.onLongPress();
                
                // Reload duration in case it changed
                SharedPreferences p = context.getSharedPreferences("OLEDPrivacyPrefs", Context.MODE_PRIVATE);
                if (isDraggable) {
                    longPressDuration = p.getInt("activation_duration", 2000);
                } else {
                    longPressDuration = p.getInt("deactivation_duration", 2000);
                }
            }
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(BUTTON_SIZE, BUTTON_SIZE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        float centerX = BUTTON_SIZE / 2f;
        float centerY = BUTTON_SIZE / 2f;
        float radius = BUTTON_SIZE / 2f - 10;
        
        switch (shape) {
            case "circle":
                canvas.drawCircle(centerX, centerY, radius, paint);
                break;
                
            case "square":
                canvas.drawRect(10, 10, BUTTON_SIZE - 10, BUTTON_SIZE - 10, paint);
                break;
                
            case "triangle":
                Path trianglePath = new Path();
                trianglePath.moveTo(centerX, 10);
                trianglePath.lineTo(BUTTON_SIZE - 10, BUTTON_SIZE - 10);
                trianglePath.lineTo(10, BUTTON_SIZE - 10);
                trianglePath.close();
                canvas.drawPath(trianglePath, paint);
                break;
                
            case "hexagon":
                Path hexPath = new Path();
                for (int i = 0; i < 6; i++) {
                    float angle = (float) (Math.PI / 3 * i);
                    float x = centerX + radius * (float) Math.cos(angle);
                    float y = centerY + radius * (float) Math.sin(angle);
                    if (i == 0) {
                        hexPath.moveTo(x, y);
                    } else {
                        hexPath.lineTo(x, y);
                    }
                }
                hexPath.close();
                canvas.drawPath(hexPath, paint);
                break;
                
            case "pentagon":
                Path pentPath = new Path();
                for (int i = 0; i < 5; i++) {
                    float angle = (float) (Math.PI / 2.5 * i - Math.PI / 2);
                    float x = centerX + radius * (float) Math.cos(angle);
                    float y = centerY + radius * (float) Math.sin(angle);
                    if (i == 0) {
                        pentPath.moveTo(x, y);
                    } else {
                        pentPath.lineTo(x, y);
                    }
                }
                pentPath.close();
                canvas.drawPath(pentPath, paint);
                break;
                
            case "star":
                Path starPath = new Path();
                for (int i = 0; i < 10; i++) {
                    float angle = (float) (Math.PI / 5 * i - Math.PI / 2);
                    float r = (i % 2 == 0) ? radius : radius / 2;
                    float x = centerX + r * (float) Math.cos(angle);
                    float y = centerY + r * (float) Math.sin(angle);
                    if (i == 0) {
                        starPath.moveTo(x, y);
                    } else {
                        starPath.lineTo(x, y);
                    }
                }
                starPath.close();
                canvas.drawPath(starPath, paint);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isDraggable) {
                    initialX = params.x;
                    initialY = params.y;
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                }
                
                // Start long press timer
                isLongPressing = true;
                longPressHandler.postDelayed(longPressRunnable, longPressDuration);
                return true;

            case MotionEvent.ACTION_MOVE:
                if (isDraggable) {
                    float deltaX = event.getRawX() - initialTouchX;
                    float deltaY = event.getRawY() - initialTouchY;
                    
                    // If moved significantly, cancel long press
                    if (Math.abs(deltaX) > 20 || Math.abs(deltaY) > 20) {
                        isLongPressing = false;
                        longPressHandler.removeCallbacks(longPressRunnable);
                    }
                    
                    params.x = (int) (initialX + deltaX);
                    params.y = (int) (initialY + deltaY);
                    currentX = params.x;
                    currentY = params.y;
                    
                    windowManager.updateViewLayout(this, params);
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isLongPressing = false;
                longPressHandler.removeCallbacks(longPressRunnable);
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void setLayoutParams(WindowManager.LayoutParams params) {
        this.params = params;
        this.currentX = params.x;
        this.currentY = params.y;
    }

    public void setDraggable(boolean draggable) {
        this.isDraggable = draggable;
        
        // Update long press duration based on state
        Context context = getContext();
        SharedPreferences prefs = context.getSharedPreferences("OLEDPrivacyPrefs", Context.MODE_PRIVATE);
        if (draggable) {
            longPressDuration = prefs.getInt("activation_duration", 2000);
        } else {
            longPressDuration = prefs.getInt("deactivation_duration", 2000);
        }
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public void updateAppearance() {
        Context context = getContext();
        SharedPreferences prefs = context.getSharedPreferences("OLEDPrivacyPrefs", Context.MODE_PRIVATE);
        color = prefs.getInt("button_color", 0xFF808080);
        shape = prefs.getString("button_shape", "circle");
        paint.setColor(color);
        invalidate();
    }
}
