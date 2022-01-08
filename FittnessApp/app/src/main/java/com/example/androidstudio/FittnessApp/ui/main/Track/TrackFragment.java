package com.example.androidstudio.FittnessApp.ui.main.Track;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import androidx.activity.OnBackPressedCallback;
import androidx.core.app.ActivityCompat;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.androidstudio.FittnessApp.R;


public class TrackFragment extends Fragment implements View.OnClickListener, LocationListener {
    private static final String TAG = "trackFragment";


    //Variablen
    boolean startStopGPS = false;

    //Widgest usw.
    private Button zoomInButton;
    private Button zoomOutButton;
    private Button centerButton;
    private Button gpsButton;
    private CompassOverlay mCompassOverlay;
    private MyLocationNewOverlay mLocationOverlay;
    private GeoPoint startPoint;
    private IMapController mapController;
    LocationManager locationManager;
    private MapView map;
    private TextView geschwindigkeitsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView():  in TrackFragment");

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d(TAG, "onBackPressed");
                AlertDialog.Builder backAlert = new AlertDialog.Builder(getActivity());
                backAlert.setTitle("Training Stoppen");
                backAlert.setMessage("Soll das Tracking wirklich gestoppt werden?");
                backAlert.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.v(TAG, "going back to home");
                        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_trackFragment_to_homeFragment);
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

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        View view = inflater.inflate(R.layout.track_fragment, container, false);
        geschwindigkeitsView = (TextView) view.findViewById(R.id.kmh);

        //Buttons anlegen und aktivieren
        zoomInButton = (Button) view.findViewById(R.id.zoomIn);
        zoomOutButton = (Button) view.findViewById(R.id.zoomOut);
        centerButton =(Button) view.findViewById(R.id.centerButton);
        gpsButton = (Button) view.findViewById(R.id.gpsButton);
        zoomOutButton.setOnClickListener(this);
        zoomInButton.setOnClickListener(this);
        centerButton.setOnClickListener(this);
        gpsButton.setOnClickListener(this);

        Context ctx = getContext().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        //Map
        map = (MapView) view.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        mapController = map.getController();
        mapController.setZoom(8);
        startPoint = new GeoPoint(48.8583, 2.2944);
        mapController.setCenter(startPoint);
        this.mCompassOverlay = new CompassOverlay(ctx, new InternalCompassOrientationProvider(ctx), map);
        this.mCompassOverlay.enableCompass();
        map.getOverlays().add(this.mCompassOverlay);
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx),map);
        map.getOverlays().add(this.mLocationOverlay);


        return view;
    }


    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick():  in TrackFragment");
        switch (view.getId()){
            case R.id.zoomIn:
                Log.d(TAG, "zoomIn in TrackFragment");
                //Button zum zoomen der Karte
                mapController.zoomIn();
                break;

            case R.id.zoomOut:
                Log.d(TAG, "zoomOut in TrackFragment");
                mapController.zoomOut();
                break;

            case R.id.centerButton:
                Log.d(TAG, "zentrierenButton in TrackFragment");
                //Button zum zentrieren der Karte

                break;

            case R.id.gpsButton:
                Log.d(TAG,"Start/Stop Button in TrackFragment");
                //Button zum starten des Trackings
                if ( ! startStopGPS) {
                    Log.d(TAG,"GPS AN");
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, TrackFragment.this);
                    }
                    this.mLocationOverlay.enableMyLocation();
                    startStopGPS = true;
                } else {
                    Log.d(TAG,"GPS AUS");
                    locationManager.removeUpdates(this);
                    this.mLocationOverlay.disableMyLocation();
                    startStopGPS = false;
                }
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }




    public void onLocationChanged(Location location) {
            Log.v(TAG, "onLocationChanged");
            getSpeed(location);
            startPoint = new GeoPoint(location);
            mapController.setCenter(startPoint);
    }

    public void getSpeed(Location location) {
        float speed = (location.getSpeed() * 3600 / 1000);
        String convertedSpeed = String.format("%.2f", speed);
        geschwindigkeitsView.setText(convertedSpeed + "km/h");
    }
}