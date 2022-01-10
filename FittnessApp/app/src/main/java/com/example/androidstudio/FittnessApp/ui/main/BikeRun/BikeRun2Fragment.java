package com.example.androidstudio.FittnessApp.ui.main.BikeRun;

import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.androidstudio.FittnessApp.R;

public class BikeRun2Fragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "hsfBikeRun2Fragment";


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView():  ");

        View view = inflater.inflate(R.layout.bike2_fragment, container, false);

        return view;
    }

    @Override
    public void onClick(View view) {

    }
}
