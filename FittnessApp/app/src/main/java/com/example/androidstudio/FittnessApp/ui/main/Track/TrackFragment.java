package com.example.androidstudio.FittnessApp.ui.main.Track;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidstudio.FittnessApp.R;


public class TrackFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "trackFragment";
    private Button zurückButton;

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


        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() in TrackFragment");

    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() in TrackFragment");

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
}