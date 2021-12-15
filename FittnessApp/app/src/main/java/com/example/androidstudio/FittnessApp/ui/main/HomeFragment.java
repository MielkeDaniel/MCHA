package com.example.androidstudio.FittnessApp.ui.main;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.androidstudio.FittnessApp.R;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "HomeFragment";
    CardView homeCard;
    CardView trackCard;
    CardView surfCard;
    CardView settingsCard;
    CardView cardioCard;
    CardView bicycleCard;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView():  ");

        View view = inflater.inflate(R.layout.home_fragment, container, false);

        homeCard = (CardView) view.findViewById(R.id.homeCard);
        trackCard = (CardView) view.findViewById(R.id.trackCard);
        surfCard = (CardView) view.findViewById(R.id.surfCard);
        settingsCard = (CardView) view.findViewById(R.id.settingsCard);
        cardioCard = (CardView) view.findViewById(R.id.cardioCard);
        bicycleCard= (CardView) view.findViewById(R.id.bicycleCard);


        homeCard.setOnClickListener(this);
        trackCard.setOnClickListener(this);
        surfCard.setOnClickListener(this);
        settingsCard.setOnClickListener(this);
        cardioCard.setOnClickListener(this);
        bicycleCard.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeCard:
                Log.v(TAG, "homecard was clicked");

                break;
            case R.id.trackCard:
                Log.v(TAG, "trackcard was clicked");

                break;
            case R.id.surfCard:
                Log.v(TAG, "surfCard was clicked");

                break;
            case R.id.settingsCard:
                Log.v(TAG, "settingsCard was clicked");

                break;
            case R.id.cardioCard:
                Log.v(TAG, "cardioCard was clicked");

                break;
            case R.id.bicycleCard:
                Log.v(TAG, "bicycleCard was clicked");

                break;

        }
    }
}