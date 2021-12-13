package com.example.androidstudio.FittnessApp;

//
// Example fuer einachen Einsatz der Navigation Components
//
// - zeigt Navigation ausgehend von Buttons
// - zeigt das Verhindern von zirkularen BackStack-Operationen
//
// 16.11.2021  tas
//

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

    }
}