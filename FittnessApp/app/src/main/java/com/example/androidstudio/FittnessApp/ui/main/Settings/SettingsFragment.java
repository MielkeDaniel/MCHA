package com.example.androidstudio.FittnessApp.ui.main.Settings;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.androidstudio.FittnessApp.R;

public class SettingsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "hsflSettingsFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView():  ");

        View view = inflater.inflate(R.layout.settings_fragments, container, false);

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
