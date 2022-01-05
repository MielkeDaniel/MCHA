package com.example.androidstudio.FittnessApp.ui.main.Settings;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.androidstudio.FittnessApp.R;

import de.hsfl.tjwa.blheartrateconnection.HeartSensorController;
import de.hsfl.tjwa.blheartrateconnection.scan.LeDeviceScanActivity;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private static String TAG = "settingsFragment";
    private static String NAME = "NAME";
    private static String AGE = "AGE";
    private static String WEIGHT = "WEIGHT";
    private static String HEIGHT = "HEIGHT";
    private static String EMAIL = "EMAIL";
    Button btButton;
    HeartSensorController heartSensorController = new HeartSensorController(getActivity());
    EditText nameInput,  ageInput, weightInput, heightInput, emailInput;
    private String currentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView():  ");

        View view = inflater.inflate(R.layout.settings_fragments, container, false);

        nameInput = (EditText) view.findViewById(R.id.nameInput);
        ageInput = (EditText) view.findViewById(R.id.ageInput);
        weightInput = (EditText) view.findViewById(R.id.weightInput);
        heightInput = (EditText) view.findViewById(R.id.heightInput);
        emailInput = (EditText) view.findViewById(R.id.emailInput);

        btButton = (Button) view.findViewById(R.id.btButton);
        btButton.setOnClickListener(this);

        loadUserFromPref();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btButton:
                Log.v(TAG, "btButton clicked");
                startActivity(new Intent(getActivity(), LeDeviceScanActivity.class));
                if(getActivity().getIntent().hasExtra(LeDeviceScanActivity.SELECTED_DEVICE)){
                    BluetoothDevice selectedHeartRateSensor = (BluetoothDevice) getActivity().getIntent().getParcelableExtra(LeDeviceScanActivity.SELECTED_DEVICE);
                    currentAdapter = selectedHeartRateSensor.getAddress();
                } else {
                    heartSensorController.startSimulation(1000);
                }
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        saveInPref();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        heartSensorController.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void saveInPref() {
        Log.v(TAG, "Saving contents");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAME, nameInput.getText().toString());
        editor.putString(AGE, ageInput.getText().toString());
        editor.putString(WEIGHT, weightInput.getText().toString());
        editor.putString(HEIGHT, heightInput.getText().toString());
        editor.putString(EMAIL, emailInput.getText().toString());
        editor.apply();
    }

    public void loadUserFromPref() {
        Log.v(TAG, "Loading Preferences");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE);
        nameInput.setText(sharedPreferences.getString(NAME, ""));
        ageInput.setText(sharedPreferences.getString(AGE, ""));
        weightInput.setText(sharedPreferences.getString(WEIGHT, ""));
        heightInput.setText(sharedPreferences.getString(HEIGHT, ""));
        emailInput.setText(sharedPreferences.getString(EMAIL, ""));
    }
}
