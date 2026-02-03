package com.oledprivacy.screen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.WindowManager;

public class BlackOverlayView extends View {

    private Paint blackPaint;
    
    public BlackOverlayView(Context context) {
        super(context);
        
        blackPaint = new Paint();
        blackPaint.setColor(Color.BLACK); // Pure black #000000 for OLED
        blackPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        // Fill entire screen with pure black
        canvas.drawRect(0, 0, getWidth(), getHeight(), blackPaint);
    }
}
