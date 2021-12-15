package com.example.androidstudio.FittnessApp.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.androidstudio.FittnessApp.R;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "hsflHelloFragment";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView():  ");

        View view = inflater.inflate(R.layout.home_fragment, container, false);

        return view;
    }

    public void onClick(View view) {
        Log.d(TAG, "onClick()");

    }

}