package com.example.androidstudio.FittnessApp.ui.main.Cardio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.androidstudio.FittnessApp.MainActivity;
import com.example.androidstudio.FittnessApp.R;

public class MyView extends View {

    private static final String TAG = "hsflMyView";
    private Paint myPaint;
    private int heartRatePosY;



    // ----- Schritt2 Hintergrundgrafik einbinden
    private Bitmap korridore;
    private Rect rView;
    private Rect rkorridore;
    // -----

    public MyView(Context context) {
        super(context);
    } //Oberklasse ird ausgerufen


    public MyView(Context context, AttributeSet attrs) {  // dieser Konstruktor wird aufgerufen, wenn das GUI-Element in der XML-Datei definiert ist
        super(context, attrs);
        Log.v(TAG, "MyView(Context context, AttributeSet attrs):  ");
        // TODO Auto-generated constructor stub


        myPaint = new Paint();

        // Paint-Objekt initialisieren
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setTextSize(20);
        myPaint.setStrokeWidth(5);
        myPaint.setColor(Color.RED);

        // ----- Schritt2 Hintergrundgrafik einbinden
        korridore = BitmapFactory.decodeResource(getResources(), R.drawable.bildmob);
        rView = new Rect();
        rkorridore = new Rect();
        rkorridore.set(0, 0, korridore.getWidth(), korridore.getHeight());
        // -----
    }


    @Override
    protected void onDraw(Canvas canvas)   // Hier die Grafik ausgeben
    {
        super.onDraw(canvas);
        Log.v(TAG, "onDraw():  ");


        rView.set(0, 0, this.getWidth(), this.getHeight());
        canvas.drawBitmap(korridore, rkorridore, rView, myPaint);

        float pixWidth = (float)getWidth();
        float pixHeight = (float)getHeight();
        Log.v(TAG, "    pixWidth: " + pixWidth + "  pixHeight: " + pixHeight );
        canvas.drawPoint( pixWidth-5, pixHeight - 5, myPaint);

        canvas.drawText("200 bqm", 10, 25, myPaint);
        canvas.drawText("50 bqm", 10, 180, myPaint);
        canvas.drawCircle(100, heartRatePosY, 10, myPaint);


        Log.v(TAG, "onDraw(canvas).......:   "+ heartRatePosY);

    }

    public void setHeartRate(int heartRate) {
        heartRatePosY=heartRate;
        Log.v(TAG, "setHeartRate().......:  "+ heartRatePosY);
        invalidate();

    }
}
