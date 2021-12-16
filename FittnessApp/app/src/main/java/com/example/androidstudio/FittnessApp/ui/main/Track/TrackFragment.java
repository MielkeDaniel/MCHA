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

import com.example.androidstudio.FittnessApp.R;


public class TrackFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "TrackFragment";
    private Button zurückButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView():  in TrackFragment");



        View view = inflater.inflate(R.layout.track_fragment, container, false);

        return view;
    }


    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick():  in TrackFragment");
        switch (view.getId()){
            case R.id.zurück: //Wenn der Startbutton gedrückt wird
                Log.d(TAG, "zurückButton");
                //Button zum wechseln zum HomeFragment
                break;
            case R.id.zentrieren:
                Log.d(TAG, "zentrierenButton");
                //Button zum zentrieren der Karte
                break;
            case R.id.start:
                Log.d(TAG,"Start/Stop Button");
                //Button zum starten des Trackings
                break;
        }

    }
}