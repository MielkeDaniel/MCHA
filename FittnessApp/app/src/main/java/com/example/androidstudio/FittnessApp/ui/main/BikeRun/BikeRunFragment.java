package com.example.androidstudio.FittnessApp.ui.main.BikeRun;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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


import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;


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

    private float speed;
    private int addedSpeed = 0;
    private double caloriesBurnt = 0;
    double distanceTraveled = 0;
    Location lastLocation;

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

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                if (timerStarted) {
                    getSpeed(location);
                    getDistance(location);
                }
            }

            public void getSpeed(Location location) {
                speed = (location.getSpeed() * 3600 / 1000);
                String convertedSpeed = String.format("%.2f", speed);
                velocity.setText(convertedSpeed);
            }

            public void getDistance(Location currentLocation) {
                    if(lastLocation != null) {
                        distanceTraveled += currentLocation.distanceTo(lastLocation) / 1000.0;
                    }
                    lastLocation = currentLocation;

                    distance.setText(String.valueOf(distanceTraveled));
            }

        };

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return view;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        return view;
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.startStop:
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
                addedSpeed = 0;
                caloriesBurnt = 0;
                distanceTraveled = 0;
                lastLocation = null;
                calories.setText("0");
                velocity.setText("0");
                distance.setText("0");
                averageVelocity.setText("Ø0");
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
                if(timerStarted){
                    calculateCalories(speed);

                    seconds++;

                    if(speed != 0.0f) {
                        addedSpeed += speed;
                        int averageSpeed = addedSpeed / seconds;
                        averageVelocity.setText("Ø" + String.valueOf(averageSpeed));
                    }
                    calories.setText(String.valueOf(caloriesBurnt));
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    public void calculateCalories(float speed){
        if(speed == 0){
            return;
        } else if(speed <= 15.0){
            caloriesBurnt += 250.0 / 3600.0;
        }else if(speed > 15.0 && speed <= 18.0){
            caloriesBurnt += 350.0 / 3600.0;
        }else if(speed > 18.0 && speed <= 22.0){
            caloriesBurnt += 500.0 / 3600.0;
        }else if(speed > 22.0 && speed <= 28.0){
            caloriesBurnt += 700.0 / 3600.0;
        }else{
            caloriesBurnt += 1000.0 / 3600.0;
        }
    }

}
