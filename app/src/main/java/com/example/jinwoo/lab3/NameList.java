package com.example.jinwoo.lab3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * En klass som sätter villkoren för utseendet för ett "item"
 * i vårt fall en NameList. Ärver från View.
 *
 * @author Jinwoo Yu
 * @version 2016.05.30
 */
public class NameList extends View {
    private String name;
    private Paint paint;
    private int screenHeight, screenWidth;

    /**
     * Konstruktorn.
     * @param theContext Context.
     * @param theName Namnet som ska sättas som ett item.
     */
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

    /**
     * Sätter textstorlek och färgar texten röd ifall det inte finns
     * några matchande namn.
     */
    public void init(){
        paint = new Paint();
        // Text storlek.
        paint.setTextSize(45f);
        // Visa röd text om namnet inte hittas.
        if(name == InteractiveSearcher.noMatchingName) {
            paint.setColor(Color.RED);
        }
    }

    /**
     * Kallas automatiskt och ritar ut texten på en canvas.
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawText(name, 10, 40, paint);
    }

    /**
     * Sätter hur stor area texten kan målas på. Är beroende
     * beroende av skärmens storlek.
     * @param widthMeasure
     * @param heightMeasure
     */
    @Override
    protected void onMeasure(int widthMeasure, int heightMeasure){
        this.setMeasuredDimension(screenWidth/2, screenHeight/25);
    }
}
