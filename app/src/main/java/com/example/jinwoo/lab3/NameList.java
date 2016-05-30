package com.example.jinwoo.lab3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by Jinwoo on 2016-03-17.
 */
// Klass som sätter villkoren för utseendet för ett ListItem.
public class NameList extends View {
    private String name;
    private Paint paint;
    private int screenHeight, screenWidth;

    public NameList(Context theContext, String theName){
        super(theContext);
        name = theName;
        // Popup-listan ska inte vara längre än halva skärmen.
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) theContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;

        init();
    }

    public void init(){
        paint = new Paint();
        // Text storlek.
        paint.setTextSize(45f);
        // Visa röd text om namnet inte hittas.
        if(name == InteractiveSearcher.noMatchingName) {
            paint.setColor(Color.RED);
        }
    }

    @Override
    protected void onDraw(Canvas canvas){
        System.out.println("name = " + name);
        canvas.drawText(name, 10, 40, paint);
    }

    @Override
    protected void onMeasure(int widthMeasure, int heightMeasure){
        this.setMeasuredDimension(screenWidth/2, screenHeight/25); // Dela på 16
    }
}
