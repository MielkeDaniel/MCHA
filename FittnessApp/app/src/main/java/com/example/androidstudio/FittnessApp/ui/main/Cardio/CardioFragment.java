package com.example.androidstudio.FittnessApp.ui.main.Cardio;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.androidstudio.FittnessApp.R;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class CardioFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "cardieFragment";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView():  ");

        View view = inflater.inflate(R.layout.cardio_fragment, container, false);
        Button butLoss = (Button)view.findViewById(R.id.butwin);
        butLoss.setOnClickListener(this);
        Button butWin = (Button)view.findViewById(R.id.butloss);
        butWin.setOnClickListener(this);

        return view;
    }

    public void onClick(View view) {
        Log.d(TAG, "onClick()");
        if (view.getId() == R.id.butwin) {
            //NavHostFragment.findNavController(this).navigate(R.id.action_gameFragment_to_winFragment);
        }
        else {
            //NavHostFragment.findNavController(this).navigate(R.id.action_gameFragment_to_lossFragment);
        }
    }
}
