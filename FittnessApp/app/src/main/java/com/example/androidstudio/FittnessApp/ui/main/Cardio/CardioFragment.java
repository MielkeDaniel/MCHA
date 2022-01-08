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

import com.example.androidstudio.FittnessApp.R;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.fragment.NavHostFragment;

import de.hsfl.tjwa.blheartrateconnection.HeartSensorController;

public class CardioFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "hsflCardioFragment";

    private static String TIME = "TIME_TAG";
    TextView timertext, textviewKcal,tv_MittlereHerz, tv_Heartrate;
    Button butpauseResume,butstartStop;
    private boolean timerunning;
    private float calories;
    private int mittlereheartrate,totalsSumm, sec, seconds, zeahler;

    HeartSensorController heartSensorController = new HeartSensorController(getActivity());

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
        heartSensorController.startSimulation(1000);
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
        return view;
    }

    public void onClick(View view) {
        Log.d(TAG, "onClick()");

        if (view.getId() == R.id.butstart) {
            if( ! butstartStop.getText().equals("Stop")) {
                timerunning=true;
                startTimer();
                Heartrate();
                butstartStop.setText("Stop");
            }
            else{
                timerunning=false;
                butstartStop.setText("Start");
                seconds=0;


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
        Handler handler= new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hrs = seconds/3600;
                int mins= (seconds%3600)/60;
                sec= seconds%60;
                Calories();
                textviewKcal.setText(" "+ calories);
                String time = String.format("%02d:%02d:%2d", hrs, mins,sec);
                timertext.setText(time);
                if (timerunning){
                    seconds++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }
    private void Calories(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE);
        //alter=getActivity().getString(AGE,"");
        int calorie=152;
        int time=15;
        float proSec;

        float proMinute= calorie/time;
        proSec= proMinute/60;
        calories = proSec*sec;

    }
    private void Heartrate(){

        Handler handler= new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                LiveData<Integer> liveData =
                        heartSensorController.getHeartRate();
                Integer heartRate = liveData.getValue();
                Log.d(TAG, "My heart rate is: " + heartRate);
                handler.post(() ->
                        tv_Heartrate.setText(String.valueOf(heartRate)));

                handler.postDelayed(this, 1000);
                int[] intArray = new int[100];
                intArray[zeahler]=heartRate;
                zeahler++;

                //Mittlere Herzfrequenz berechnen
                for(int i=0;i< intArray.length; i++){
                    totalsSumm= totalsSumm+intArray[i];
                    Log.d(TAG, "Array: " + intArray[i]);
                    mittlereheartrate= totalsSumm/(zeahler);
                    Log.d(TAG, "Total wäre: " + mittlereheartrate);
                    String stringmittlereheartrate = String.valueOf(mittlereheartrate);

                    tv_MittlereHerz.setText(stringmittlereheartrate);
                }

            }

        });

    }


}



    //@Override
   // public void onStop() {
       // super.onStop();
        //saveInPref();
    //}
    //public void saveInPref() {
        //Log.v(TAG, "Saving contents");
        //SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE);
       // SharedPreferences.Editor editor = sharedPreferences.edit();
       // editor.putString(TIME, timertext.getText().toString());

       // editor.apply();
    //}
   // public void loadUserFromPref() {
       // Log.v(TAG, "Loading Preferences");
       // SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE);
        //timertext.setText(sharedPreferences.getString(TIME, ""));

    //}


