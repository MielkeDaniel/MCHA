package com.example.androidstudio.FittnessApp.ui.main.BikeRun;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidstudio.FittnessApp.R;

import androidx.fragment.app.Fragment;

import android.content.Context;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

public class BikeRunFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "hsfBikeRunFragment";

    private Button startStopButton;
    private Button resetButton;

    private TextView velocity;
    private TextView averageVelocity;
    private TextView bpm;
    private TextView averageBPM;
    private TextView calories;
    private TextView distance;
    private TextView duration;

    private int addedSpeed = 0;
    private int Calories = 0;

    private int seconds = 0;
    boolean timerStarted = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView():  ");

        View view = inflater.inflate(R.layout.bike_fragment, container, false);

        startStopButton = (Button) view.findViewById(R.id.startStop);
        resetButton = (Button) view.findViewById(R.id.reset);

        velocity = (TextView) view.findViewById(R.id.velocity);
        averageVelocity = (TextView) view.findViewById(R.id.averageVelocity);
        bpm = (TextView) view.findViewById(R.id.bpm);
        averageBPM = (TextView) view.findViewById(R.id.averageBPM);
        calories = (TextView) view.findViewById(R.id.calories);
        distance = (TextView) view.findViewById(R.id.distance);
        duration = (TextView) view.findViewById(R.id.duration);

        startStopButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);

        startTimer();

        return view;
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.startStop:
                Log.d(TAG, "onClick.startStop");
                if (timerStarted == false) {
                    timerStarted = true;
                    startStopButton.setText("Stop");
                    startStopButton.setBackgroundColor(Color.RED);
                } else {
                    timerStarted = false;
                    startStopButton.setText("Start");
                    startStopButton.setBackgroundColor(Color.GREEN);
                }
                break;

            case R.id.reset:
                Log.d(TAG, "onClick.reset");
                timerStarted = false;
                seconds = 0;
                startStopButton.setText("Start");
                startStopButton.setBackgroundColor(Color.GREEN);
                break;
        }
    }


    private void startTimer() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            public void run () {
                int hours = seconds / 3600;
                int mins = (seconds % 3600) / 60;
                int secs = seconds % 60;
                String time = String.format("%02d, %02d, %02d", hours, mins, secs);
                duration.setText(time);
                int speed = location.getSpeed();
                if(timerStarted){
                    addedSpeed += speed;
                    int averageSpeed = addedSpeed / seconds;
                    seconds++;
                    velocity.setText(String.valueOf(speed));
                    averageVelocity.setText(String.valueOf(averageSpeed));
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void calculateCalories(int speed){
        if(speed <= 15){

        }else if(speed > 15 && speed <= 18){

        }else if(speed > 18 && speed <= 22){

        }else if(speed > 22 && speed <= 28){

        }else{

        }
    }

}
