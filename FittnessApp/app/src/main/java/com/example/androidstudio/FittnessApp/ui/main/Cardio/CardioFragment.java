package com.example.androidstudio.FittnessApp.ui.main.Cardio;


import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidstudio.FittnessApp.R;

import androidx.fragment.app.Fragment;

public class CardioFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "hsflGameFragment";
    TextView timertext;
    Button butpauseResume;
    Button butstartStop;
    private int seconds;
    private boolean timerunning;
    private double calories;
    private int sec;
    TextView textviewKcal;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView():  ");

        View view = inflater.inflate(R.layout.cardio_fragment, container, false);
        startTimer();

        timertext= (TextView) view.findViewById(R.id.dauerZeit);
        textviewKcal= (TextView) view.findViewById(R.id.tvKcal);

        butpauseResume = (Button) view.findViewById(R.id.butpause);
        butpauseResume.setOnClickListener(this);
        butstartStop = (Button) view.findViewById(R.id.butstart);
        butstartStop.setOnClickListener(this);

        return view;
    }

    public void onClick(View view) {
        Log.d(TAG, "onClick()");

        if (view.getId() == R.id.butstart) {
            if( ! butstartStop.getText().equals("Stop")) {
                timerunning=true;
                butstartStop.setText("Stop");

            }
            else{
                timerunning=false;
                butstartStop.setText("Start");
                //seconds=0;
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

        calories = 0.16880*sec;
    }
}
