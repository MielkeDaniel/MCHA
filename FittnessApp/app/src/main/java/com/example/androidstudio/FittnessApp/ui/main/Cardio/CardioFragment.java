package com.example.androidstudio.FittnessApp.ui.main.Cardio;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidstudio.FittnessApp.MainActivity;
import com.example.androidstudio.FittnessApp.R;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import de.hsfl.tjwa.blheartrateconnection.HeartSensorController;

public class CardioFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "hsflCardioFragment";

    private static String TIME = "TIME_TAG";
    TextView timertext, textviewKcal,tv_MittlereHerz, tv_Heartrate;
    Button butpauseResume,butstartStop;
    private boolean timerunning;
    private float calories;
    private int totalHeartRate, sec, seconds, mittlereheartrate;
    private String weight;
    private float met = 6; // MET Maßeinheit für die Intensität von Bewegung oder Sport (Rudern mit Rudergerät)
    private String gender;
    float  proHrs;
    float proSec;
    private int heartRate;
    HeartSensorController heartSensorController;
    static private MyView viewKorridor;
    private int heartRateZeahler=0;



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView():  ");

        View view = inflater.inflate(R.layout.cardio_fragment, container, false);

        timertext= (TextView) view.findViewById(R.id.dauerZeit);
        textviewKcal= (TextView) view.findViewById(R.id.tvKcal);
        tv_Heartrate= (TextView) view.findViewById(R.id.tvHerzfrequenz);
        tv_MittlereHerz= (TextView) view.findViewById(R.id.tvMittlereherz);
        butpauseResume = (Button) view.findViewById(R.id.butpause);
        butpauseResume.setOnClickListener(this);
        butstartStop = (Button) view.findViewById(R.id.butstart);
        butstartStop.setOnClickListener(this);

        heartSensorController = ((MainActivity)getActivity()).getHeartSensorController();
        if (heartSensorController.isConnected()) {
            heartSensorController.startBluetooth(true);
        } else {
            heartSensorController.startSimulation(1000);
        }

        viewKorridor = view.findViewById(R.id.heartrateView);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE);
        weight = sharedPreferences.getString("WEIGHT",String.valueOf(0));
        gender = sharedPreferences.getString("GENDER", "");



        //loadUserFromPref();

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
                        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_cardioFragment_to_home_fragment);
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

        Log.v(TAG,"TTTTTTTTTTTTT" + String.valueOf(((MainActivity) getActivity()).getHeartSensorController().getHeartRate().getValue()));
        return view;
    }

    public void onClick(View view) {
        Log.d(TAG, "onClick()");

        if (view.getId() == R.id.butstart) {
            if( ! butstartStop.getText().equals("Stop")) {
                timerunning=true;
                startTimer();
                butstartStop.setText("Stop");
            }
            else {
                timerunning=false;
                butstartStop.setText("Start");
            }
        }
        else if (view.getId() == R.id.butpause){
            if( ! butpauseResume.getText().equals("Pause")) {
                timerunning=true;
                butpauseResume.setText("Pause");
            }
            else{
                timerunning=false;
                butpauseResume.setText("Resume");
            }
        }
    }
    private void startTimer(){
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hrs = seconds/3600;
                int mins= (seconds%3600)/60;
                sec= seconds%60;
                String time = String.format("%02d:%02d:%2d", hrs, mins,sec);
                if (timerunning){
                    seconds++;
                    Calories();
                    textviewKcal.setText(" "+ calories);
                    timertext.setText(time);
                    Heartrate();
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    private void Calories(){
        if (gender.equals("MALE")){
            proHrs = (float) (0.9*1*Float.parseFloat(weight)*met); //Calores in 1 sec
            proSec = proHrs/3600;
            calories = proSec*sec;
        }
        else if(gender.equals("FEMALE")){
            proHrs = 1 * (1/3600) * Float.parseFloat(weight) * met;
            proSec = proHrs/3600;
            calories = proSec*sec;

            Log.v(TAG, "CALORIES:" + calories + " proHrs:" + proHrs + " proSec:" + proSec + " met:" + met + " weight:" + Float.parseFloat(weight));

        }
        else if (gender.equals("DIVERS")){
            proHrs = (float) (0.9*1*Float.parseFloat(weight)*met); //Calores in 1 sec
            proSec = proHrs/3600;
            calories = proSec*sec;
        }
    }

    private void Heartrate() {


        heartRate = heartSensorController.getHeartRate().getValue();

        heartRateZeahler++;

        Log.d(TAG, "My heart rate is: " + heartRate);
        tv_Heartrate.setText(String.valueOf(heartRate+" bqm"));

        viewKorridor.setHeartRate(heartRate);

        totalHeartRate+=heartRate;
        mittlereheartrate=totalHeartRate/heartRateZeahler;
        tv_MittlereHerz.setText(String.valueOf(mittlereheartrate+" bqm"));

        viewKorridor.invalidate();


    }

}




