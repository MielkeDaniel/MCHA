package com.example.androidstudio.FittnessApp.ui.main.Track;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidstudio.FittnessApp.R;


public class TrackFragment extends Fragment implements View.OnClickListener, SensorEventListener {
    private static final String TAG = "TrackFragment";
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private Button zoomButton;
    private Button centerButton;
    private Button gpsButton;

    private TextView geschwindigkeitsView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView():  in TrackFragment");

        View view = inflater.inflate(R.layout.track_fragment, container, false);
        geschwindigkeitsView = (TextView) view.findViewById(R.id.kmh);

        //Buttons anlegen und aktivieren
        zoomButton = (Button) view.findViewById(R.id.zoom);
        centerButton =(Button) view.findViewById(R.id.zentrieren);
        gpsButton = (Button) view.findViewById(R.id.gpsButton);
        zoomButton.setOnClickListener(this);
        centerButton.setOnClickListener(this);
        gpsButton.setOnClickListener(this);

        //Sensormanager
        sensorManager = (SensorManager)this.getActivity().getSystemService(Activity.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() in TrackFragment");
        sensorManager.registerListener(this, sensorManager.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION).get(0), SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() in TrackFragment");
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick():  in TrackFragment");
        switch (view.getId()){
            case R.id.zoom:
                Log.d(TAG, "zoomButton in TrackFragment");
                //Button zum zoomen der Karte

                break;
            case R.id.zentrieren:
                Log.d(TAG, "zentrierenButton in TrackFragment");
                //Button zum zentrieren der Karte

                break;
            case R.id.gpsButton:
                Log.d(TAG,"Start/Stop Button in TrackFragment");
                //Button zum starten des Trackings

                break;
        }

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.v(TAG, "onSensorChanged in TrackFragment");

        //Variablen für die Geschwindigkeit auf den verschiedenen Achsen
        Float xA = sensorEvent.values[0];
        Float yA = sensorEvent.values[1];
        Float zA = sensorEvent.values[2];

        geschwindigkeitsView.setText(zA.toString());

        /* Berechnung der Geschwindigkeit von m/s in km/h
        Berechung usw.
         */

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        /* braucht man i.d.R. nicht, muss aber
           fuer das Interface SensorEventListener
           vorhanden sein */
        Log.d(TAG,"onAccuracyChanged in Trackfragment");
    }
}