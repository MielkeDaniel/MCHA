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
import org.osmdroid.views.overlay.Polyline;
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

import java.util.ArrayList;
import java.util.List;


public class TrackFragment extends Fragment implements View.OnClickListener, LocationListener {
    private static final String TAG = "trackFragment";


    //Boolean-Variablen
    boolean startStopGPS = false;
    boolean center = true;
    //Buttons
    private Button zoomInButton;
    private Button zoomOutButton;
    private Button centerButton;
    private Button gpsButton;
    //Overlays
    private CompassOverlay mCompassOverlay;
    private MyLocationNewOverlay mLocationOverlay;
    //Location,Zeichnen usw.
    LocationManager locationManager;
    private GeoPoint point;
    private IMapController mapController;
    private MapView map;
    private  Polyline line;
    private List<GeoPoint> geoPoints;
    //Textviews
    private TextView geschwindigkeitsView;
    private TextView centerDescription;
    private TextView gpsButtonDescription;

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
        centerDescription = (TextView) view.findViewById(R.id.centerDescription);
        gpsButtonDescription = (TextView) view.findViewById(R.id.gpsButtonDescription);
        zoomOutButton.setOnClickListener(this);
        zoomInButton.setOnClickListener(this);
        centerButton.setOnClickListener(this);
        gpsButton.setOnClickListener(this);

        Context ctx = getContext().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        //Map
        map = (MapView) view.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        //Mapcontroller
        mapController = map.getController();
        mapController.setZoom(8);
        point = new GeoPoint(52.106701, 10.198094);
        mapController.setCenter(point);
        //Compassoverlay
        this.mCompassOverlay = new CompassOverlay(ctx, new InternalCompassOrientationProvider(ctx), map);
        this.mCompassOverlay.enableCompass();
        map.getOverlays().add(this.mCompassOverlay);
        //MyLocationOverlay
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(ctx),map);
        map.getOverlays().add(this.mLocationOverlay);
        //Polylinevariablen
        geoPoints = new ArrayList<>();
        line = new Polyline();
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
                if(center) {
                    this.mLocationOverlay.disableFollowLocation();
                    center = false;
                    centerDescription.setText("Dezentriert");
                } else {
                    this.mLocationOverlay.enableFollowLocation();
                    centerDescription.setText("Zentriert");
                    center = true;
                }
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
                    gpsButtonDescription.setText("GPS aktiv");
                    gpsButton.setText("Stop");
                    this.mLocationOverlay.enableFollowLocation();
                } else {
                    Log.d(TAG,"GPS AUS");
                    locationManager.removeUpdates(this);
                    this.mLocationOverlay.disableMyLocation();
                    startStopGPS = false;
                    gpsButtonDescription.setText("GPS inaktiv");
                    gpsButton.setText("Start");
                }
                break;
        }
    }
    @Override
    public void onResume() {
        Log.d(TAG, "onResume() in TrackFragment");
        super.onResume();
        this.mLocationOverlay.disableMyLocation();
        locationManager.removeUpdates(this);
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause() in TrackFragment");
        super.onPause();
        this.mLocationOverlay.disableMyLocation();
        locationManager.removeUpdates(this);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onStart(){
        Log.d(TAG, "onStart() in TrackFragment");
        super.onStart();
        this.mLocationOverlay.disableMyLocation();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStop(){
        Log.d(TAG, "onStop() in TrackFragment");
        super.onStop();
        this.mLocationOverlay.disableMyLocation();
        locationManager.removeUpdates(this);
    }


    public void onLocationChanged(Location location) {
            Log.v(TAG, "onLocationChanged in Tragfragment");
            getSpeed(location);
            point = new GeoPoint(location);
            geoPoints.add(point);

            //Füllen der Liste und Zeichnen der Linie
            if(geoPoints.size() >= 2) {
                line.setPoints(geoPoints);
                map.getOverlayManager().add(line);
            }
    }

    public void getSpeed(Location location) {
        Log.v(TAG, "getSpeed in TrackFragment");
        float speed = (location.getSpeed() * 3600 / 1000);
        String convertedSpeed = String.format("%.2f", speed);
        geschwindigkeitsView.setText(convertedSpeed + "km/h");
    }
}