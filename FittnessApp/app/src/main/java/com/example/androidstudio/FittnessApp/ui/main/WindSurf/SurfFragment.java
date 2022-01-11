package com.example.androidstudio.FittnessApp.ui.main.WindSurf;



import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.androidstudio.FittnessApp.R;



public class SurfFragment extends Fragment implements View.OnClickListener, LocationListener , SensorEventListener{
    private static final String TAG = "hsfWindFragment";
    private Button startBtn;
    private Button resBtn;
    Location lastLoc;
    //****************************  compass
    float mAzimuth;
    String where , whereav , aaaa;
    private ImageView mPointer;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private float mCurrentDegree = 0f;
    //***********************************End compass
    private TextView speed1 ;
    private TextView avkurs;
    private TextView averagespeed;
    private TextView weg;
    private TextView timepassed;
    private TextView kurs;
    float av ;
    private float speed2 ;
    double  wegtraveled = 0 ;
    private int sec = 0;
    private int sse = sec / 15 ;
    boolean timeStart = false;
    LocationManager locManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView():  ");

        View view = inflater.inflate(R.layout.surf_fragment, container, false);


        //***************************************** compass
        mSensorManager = (SensorManager)  getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mPointer = (ImageView) view.findViewById(R.id.campssimg);
        //***********************************************************************************************************



        startBtn = (Button) view.findViewById(R.id.startbtn1);
        averagespeed = (TextView) view.findViewById(R.id.avaregespeedText);
        avkurs = (TextView)  view.findViewById(R.id.avaregespeedText);
        weg = (TextView) view.findViewById(R.id.destanceText);
        timepassed = (TextView) view.findViewById(R.id.durationsurf);
        resBtn = (Button) view.findViewById(R.id.resetbtn);
        speed1 = (TextView) view.findViewById(R.id.txtspeed);
        startBtn.setOnClickListener(this);
        resBtn.setOnClickListener(this);
        kurs = (TextView) view.findViewById(R.id.nwtxt);


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d(TAG, "onBackPressed");
                AlertDialog.Builder backAlert = new AlertDialog.Builder(getActivity());
                backAlert.setTitle("Training Stop ?");
                backAlert.setMessage("would you like to stop surfing ?");
                backAlert.setPositiveButton("Yup", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.v(TAG, "going back to home");
                        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_action_homeFragment_to_surfFragment_to_homeFragment);
                    }
                });

                backAlert.setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                backAlert.show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), callback);

        startTimer();
        locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);


        return view;
    }

    private void startTimer() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            public void run() {
                int hours = sec / 3600;
                int mints = (sec % 3600) / 60;
                int secs = sec % 60;
                int ssee = mints / 4 ;
                String time = String.format("%02d, %02d, %02d", hours, mints, secs);
                timepassed.setText(time);
                if(timeStart){


                    sec++;}


                handler.postDelayed(this, 1000);
            }


        });
    }
    //********************************compass
    private void startsensor(){

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
    }
   private void stopSensor(){
       mSensorManager.unregisterListener(this, mAccelerometer);
       mSensorManager.unregisterListener(this, mMagnetometer);

    }



    @Override
    public void onLocationChanged(Location location) {
        if (timeStart) {
            Log.v(TAG, "onLocationChanged");
            getSpeed(location);
            getDistance(location);
        }
    }
    //******************************

    public void getSpeed(Location location) {
        speed2 = (location.getSpeed() * 3600 / 1000);
        String convertedSpeed = String.format("%.2f", speed2);
        speed1.setText(convertedSpeed);

    }


    public void onClick(View view){
        switch (view.getId()) {
            case R.id.startbtn1:
                Log.d(TAG, "onClick.startStop");
                if (timeStart == false) {
                    timeStart = true;
                    startBtn.setText("Stop");
                    startBtn.setBackgroundColor(Color.RED);
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, SurfFragment.this);


                        startsensor();//r
                    }

                } else {
                    timeStart = false;
                    startBtn.setText("Start");
                    startBtn.setBackgroundColor(Color.BLUE);
                    stopSensor();

                }
                break;

            case R.id.resetbtn:
                Log.d(TAG, "onClick.reset");
                AlertDialog.Builder resetAlert = new AlertDialog.Builder(getActivity());
                resetAlert.setTitle("reset?");
                resetAlert.setMessage("Are you SURE You want to reset ?");
                resetAlert.setPositiveButton("YUP", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "reset all values");
                        resetall();

                        stopSensor();
                    }
                });

                resetAlert.setNeutralButton("Cancell", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                resetAlert.show();
                break;
        }
    }
    public void getDistance(Location userloc) {

        if(lastLoc != null) {
            wegtraveled += userloc.distanceTo(lastLoc) / 100.0;
        }
        lastLoc = userloc;

        weg.setText(String.valueOf(wegtraveled  ));
    }

    private void resetall() {

        timeStart = false;
        sec = 0;
        wegtraveled = 0;


        speed1.setText("0");
        weg.setText("0");

        averagespeed.setText("0");

        startBtn.setText("Start");
        startBtn.setBackgroundColor(Color.BLUE);
    }


    //******************************************compass
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor == mAccelerometer  ) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;

        } else if (event.sensor == mMagnetometer) {

            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;

        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(mR, mOrientation);



            float azimuthInRadians = mOrientation[0];
            float azimuthInDegress = -(float)(Math.toDegrees(azimuthInRadians))%360;
            RotateAnimation ra = new RotateAnimation(
                    mCurrentDegree,
                    -azimuthInDegress,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f);


            av = (mAzimuth %sec) %2 ;

            ra.setDuration(250);


            mAccelerometer.getFifoMaxEventCount();




            ra.setFillAfter(true);
            kurs.setText("" + azimuthInDegress + "°" + where);

            mAzimuth = azimuthInDegress;







            avkurs.setText("" + whereav );


//**********************************************************************************************

            //*****************************************
            if (mAzimuth >= 0 || mAzimuth <= 0  )
                where = "N";
            else if (mAzimuth < -90 && mAzimuth > -180  )
                where = "SW";
          else   if (mAzimuth <= 99 && mAzimuth > 80)
                where = "E";
           else  if (mAzimuth < 90 && mAzimuth > 0 )
                where = "NE";
            else if (mAzimuth <= -180&& mAzimuth > -190)
                where = "S";
            else if (mAzimuth > 99 && mAzimuth < 180)
                where = "SE";
            else  if (mAzimuth <= -74 && mAzimuth > -100)
                where = "W";
            else if (mAzimuth < 0 && mAzimuth > - 90)
                where = "NE";

            if (av >= 0 || av <= 0  )
                whereav = "N";
            else  if (av < -90 && av > -180  )
                whereav = "SW";
            else  if (av <= 99 && av > 80)
                whereav = "E";
            else  if (av < 90 && av > 0 )
                whereav = "NW";
            else  if (av <= -180&& av > -190)
                whereav = "S";
            else   if (av > 99 && av < 180)
                whereav = "SE";
            else   if (av <= -74 && av > -100)
                whereav = "W";
            else   if (av < 0 && av > - 90)
                whereav = "NE";

            mPointer.startAnimation(ra);
            mCurrentDegree = -azimuthInDegress;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    //********************************************************
}


