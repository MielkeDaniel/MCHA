package com.example.androidstudio.FittnessApp.ui.main.Track;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;

import com.example.androidstudio.FittnessApp.R;


public class TrackFragment extends Fragment implements View.OnClickListener, LocationListener {

    private static final String TAG = "trackFragment";
    private Button zurückButton;

    private Button zoomButton;
    private Button centerButton;
    private Button gpsButton;

    private MapView map;

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

        //Map
        Context ctx = getActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        getActivity().setContentView(R.layout.track_fragment);

        map = (MapView) view.findViewById(R.id.mapview);
        map.setTileSource(TileSourceFactory.MAPNIK);



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

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }
}