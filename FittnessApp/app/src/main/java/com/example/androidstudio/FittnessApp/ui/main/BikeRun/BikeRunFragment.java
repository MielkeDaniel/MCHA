package com.example.androidstudio.FittnessApp.ui.main.BikeRun;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidstudio.FittnessApp.R;


import androidx.activity.OnBackPressedCallback;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Context;

import de.hsfl.tjwa.blheartrateconnection.HeartSensorController;


public class BikeRunFragment extends Fragment implements View.OnClickListener, LocationListener {
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

    HeartSensorController heartSensorController = new HeartSensorController(getActivity());

    private float speed;
    private int addedSpeed = 0;
    private double caloriesBurnt = 0;
    private int addedBPM = 0;
    double distanceTraveled = 0;
    Location lastLocation;

    private int seconds = 0;
    boolean timerStarted = false;

    LocationManager locationManager;

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

        heartSensorController.startSimulation(1000);

        startTimer();

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d(TAG, "onBackPressed");
                AlertDialog.Builder backAlert = new AlertDialog.Builder(getActivity());
                backAlert.setTitle("Training Stoppen");
                backAlert.setMessage("Soll das Training wirklich gestoppt werden?");
                backAlert.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.v(TAG, "going back to home");
                        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_bikeRunFragment_to_homeFragment);
                    }
                });

                backAlert.setNeutralButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                    }
                });

                backAlert.show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

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
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, BikeRunFragment.this);
                    }
                } else {
                    timerStarted = false;
                    startStopButton.setText("Start");
                    startStopButton.setBackgroundColor(Color.GREEN);
                }
                break;

            case R.id.reset:
                Log.d(TAG, "onClick.reset");
                AlertDialog.Builder resetAlert = new AlertDialog.Builder(getActivity());
                resetAlert.setTitle("Zurücksetzen");
                resetAlert.setMessage("Soll das Training wirklich zurückgesetzt werden?");
                resetAlert.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "resetting");
                        resetValues();
                    }
                });

                resetAlert.setNeutralButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                    }
                });

                resetAlert.show();
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
                    int heartrate = heartSensorController.getHeartRate().getValue();
                    addedBPM += heartrate;
                    int averageHeartrate = addedBPM / seconds;

                    if(speed != 0.0f) {
                        addedSpeed += speed;
                        int averageSpeed = addedSpeed / seconds;
                        averageVelocity.setText("Ø" + String.valueOf(averageSpeed));
                    }
                    bpm.setText(String.valueOf(heartrate));
                    averageBPM.setText(String.valueOf("Ø" + averageHeartrate));
                    calories.setText(String.valueOf(caloriesBurnt));
                }
                handler.postDelayed(this, 1000);
            }
        });
    }



        @Override
        public void onLocationChanged(Location location) {
            if (timerStarted) {
                Log.v(TAG, "onLocationChanged");
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

    public void resetValues(){
        timerStarted = false;
        seconds = 0;
        addedSpeed = 0;
        addedBPM = 0;
        caloriesBurnt = 0;
        distanceTraveled = 0;
        lastLocation = null;
        calories.setText("0");
        velocity.setText("0");
        distance.setText("0");
        bpm.setText("0");
        averageVelocity.setText("Ø0");
        averageBPM.setText("Ø0");
        startStopButton.setText("Start");
        startStopButton.setBackgroundColor(Color.GREEN);
    }


}
