package com.example.androidstudio.FittnessApp.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.androidstudio.FittnessApp.R;


public class TrackFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "TrackFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView():  in TrackFragment");

        View view = inflater.inflate(R.layout.track_fragment, container, false);

        return view;
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onCreateView():  in TrackFragment");
        //Wird alles noch gemacht :D
    }
}