package com.example.androidstudio.FittnessApp.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.androidstudio.FittnessApp.R;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class BikeRunFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "hsfLossFragment";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView():  ");

        View view = inflater.inflate(R.layout.bike_fragment, container, false);
        //Button but = (Button)view.findViewById(R.id.butnewgame2);
        //but.setOnClickListener(this);

        return view;
    }

    public void onClick(View view) {
        Log.d(TAG, "onClick()");
        NavHostFragment.findNavController(this).navigate(R.id.action_lossFragment_to_helloFragment);
    }

}