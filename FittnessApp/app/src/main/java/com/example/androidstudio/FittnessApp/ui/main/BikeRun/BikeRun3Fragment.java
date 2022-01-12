package com.example.androidstudio.FittnessApp.ui.main.BikeRun;

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
import com.example.androidstudio.FittnessApp.ui.main.Cardio.MyView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import de.hsfl.tjwa.blheartrateconnection.HeartSensorController;

public class BikeRun3Fragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "hsflBikeRun3Fragment";


    private TextView timertext, textviewKcal,tv_MittlereHerz, tv_Heartrate;
    private Button butpauseResume,butstartStop, bikeRun2Button;
    private boolean timerunning;
    private double calories, perHour, perSec;
    private int totalHeartRate, sec, seconds, averageHeartrate;

    private float met = 6; // MET Unit of measurement for the intensity of movement or sport (rowing with rowing machine).
    private boolean ismen, iswomen, isdev; //Gender of the users
    private String weight;


    private int heartRate;
    HeartSensorController heartSensorController;
    static private MyView viewKorridor;
    private int heartRateZeahler=0;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView():  ");

        //.........Connect GUI elements
        View view = inflater.inflate(R.layout.bike3_fragment, container, false);
        timertext= (TextView) view.findViewById(R.id.dauerZeit);
        textviewKcal= (TextView) view.findViewById(R.id.tvKcal);
        tv_Heartrate= (TextView) view.findViewById(R.id.tvHerzfrequenz);
        tv_MittlereHerz= (TextView) view.findViewById(R.id.tvMittlereherz);
        butpauseResume = (Button) view.findViewById(R.id.butpause);
        butpauseResume.setOnClickListener(this);
        butstartStop = (Button) view.findViewById(R.id.butstart);
        bikeRun2Button = (Button) view.findViewById(R.id.backToBikeRun2);
        butstartStop.setOnClickListener(this);
        bikeRun2Button.setOnClickListener(this);

        //initialisieren des heartSensors aus der MainActivity
        //wenn kein Herzratensensor vebunden ist werden Werte simuliert, um die Funktion der App aufrecht zu erhalten
        heartSensorController = ((MainActivity)getActivity()).getHeartSensorController();
        if (heartSensorController.isConnected()) {
            heartSensorController.startBluetooth(true);
        } else {
            heartSensorController.startSimulation(1000);
        }

        //Reference on the view
        viewKorridor = view.findViewById(R.id.heartrateView);

        //Input of the user SettingsFragment are fetched via SharedPreferences and stored here
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE);
        weight = sharedPreferences.getString("WEIGHT",String.valueOf(0));
        ismen=sharedPreferences.getBoolean("MALE", false);
        iswomen=sharedPreferences.getBoolean("FAMALE",false);
        isdev=sharedPreferences.getBoolean("DIVERS", false);

        //Back button is installed and asks the user if he wants to leave the page
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d(TAG, "onBackPressed");
                AlertDialog.Builder backAlert = new AlertDialog.Builder(getActivity());
                backAlert.setTitle("Zurück zu BikeRun2");
                backAlert.setMessage("Soll das Training wirklich gestoppt werden?");
                backAlert.setPositiveButton("Ja", new DialogInterface.OnClickListener() {

                    @Override
                    //When this is confirmed, the user leaves Cardio and goes to HomeFragment.
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.v(TAG, "going back to BikeRun2");
                        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_bikeRun3Fragment_to_bikeRun2Fragment);
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

    //When the GUI button is clicked
    public void onClick(View view) {
        Log.d(TAG, "onClick()");

        if (view.getId() == R.id.butstart) {
            if( ! butstartStop.getText().equals("Stop")) {
                timerunning=true; //Wenn The Boolean becomes True the Timer will start
                startTimer();
                butstartStop.setText("Stop");//Text on the button is changed to Stop
            }
            else {
                butstartStop.setText("Start");
                timerunning=false;

                //Ask the user if he wants to stop drinking
                AlertDialog.Builder resetAlert = new AlertDialog.Builder(getActivity());//
                resetAlert.setTitle("Stoppen");
                resetAlert.setMessage("Soll das Training wirklich gestoppt werden?");
                resetAlert.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d(TAG, "Stopp");
                        Stop();
                    }
                });
                resetAlert.setNeutralButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //do nothing
                    }
                });
                resetAlert.show();
            }
        }

        else if (view.getId() == R.id.butpause){ //Pause
            if( ! butpauseResume.getText().equals("Pause")) {
                timerunning=true;
                butpauseResume.setText("Pause"); //Pause the trening
            }
            else{
                timerunning=false; //Resume the trening
                butpauseResume.setText("Resume");
            }
        }else if (view.getId() == R.id.backToBikeRun2){
            Log.d(TAG,"onClick BikeRun2");
            AlertDialog.Builder bike2Alert = new AlertDialog.Builder(getActivity());
            bike2Alert.setTitle("Zurück zu BikeRun2");
            bike2Alert.setMessage("Soll das Training wirklich gestoppt werden?");
            bike2Alert.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Log.v(TAG, "going to BikeRun2");
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.action_bikeRun3Fragment_to_bikeRun2Fragment);
                }
            });

            bike2Alert.setNeutralButton("Abbrechen", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //do nothing
                }
            });

            bike2Alert.show();
        }
    }
    // The Handler helps to reckoned the time. with a Thread´s.
    private void startTimer(){
        Log.d(TAG, "StartTimer(); ");
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hrs = seconds / 3600;
                int mins= (seconds % 3600) / 60;
                sec= seconds % 60;
                String time = String.format("%02d:%02d:%2d", hrs, mins,sec);

                if (timerunning){
                    seconds++;
                    Calories(); //The calorie class is called after every second
                    textviewKcal.setText(" "+ Math.round(calories)); // The calories are visible to the user on the textview
                    timertext.setText(time);
                    Heartrate();
                }
                handler.postDelayed(this, 1000); //after every second this class is called
            }
        });
    }

    private void Calories(){
        Log.d(TAG, "Calories() " );
        //https://www.rezeptrechner-online.de/blog/tag/aktivitaetsfaktor/
        //Calorie consumption for the sport rowing
        //first it will ask for the input of the usach and get from SettingsFragment the weight and the gender
        if (ismen==true){
            perHour=0.9*1*Double.parseDouble(weight)*met; //Calores in 1 sec
            perSec=perHour/3600;
            calories=perSec*sec;
        }
        else if(iswomen==true){
            perHour=1*(1/3600)*Double.parseDouble(weight)*met;
            perSec=perHour/3600;
            calories=perSec*sec;

        }
        else if (isdev==true){
            perHour=0.9*1*Double.parseDouble(weight)*met; //Calores in 1 sec
            perSec=perHour/3600;
            calories=perSec*sec;

        }
    }

    private void Heartrate() {
        Log.d(TAG, "Heartrate(); " );
        //The heart rate that runs over a simulation is called on and from it the value is stored in this class
        heartRate = ((MainActivity) getActivity()).getHeartSensorController().getHeartRate().getValue();
        heartRateZeahler++;
        tv_Heartrate.setText(String.valueOf(heartRate+" bqm"));
        viewKorridor.setHeartRate(heartRate, heartRateZeahler);

        totalHeartRate+=heartRate;
        averageHeartrate =totalHeartRate/heartRateZeahler;
        tv_MittlereHerz.setText(String.valueOf(averageHeartrate +" bqm"));
    }
    private void Stop(){
        Log.d(TAG, "Stop(); " );
        //resets everything
        seconds=0;
        textviewKcal.setText("0.0");
        tv_Heartrate.setText("0");
        timertext.setText("00:00:00");
        tv_MittlereHerz.setText("0");
        heartRate=0;
        heartRateZeahler=0;

    }
}
