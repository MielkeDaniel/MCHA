package com.example.androidstudio.FittnessApp.ui.main.Settings;

import static android.app.Activity.RESULT_OK;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.androidstudio.FittnessApp.MainActivity;
import com.example.androidstudio.FittnessApp.R;

import de.hsfl.tjwa.blheartrateconnection.HeartSensorController;
import de.hsfl.tjwa.blheartrateconnection.scan.LeDeviceScanActivity;

public class SettingsFragment extends Fragment implements View.OnClickListener {


    // Shared Preferences Keys
    private static String TAG = "settingsFragment";
    private static String NAME = "NAME";
    private static String AGE = "AGE";
    private static String WEIGHT = "WEIGHT";
    private static String HEIGHT = "HEIGHT";
    private static String EMAIL = "EMAIL";
    private static String MALE = "MALE";
    private static String FEMALE = "FEMALE";
    private static String DIVERS = "DIVERS";

    private static int request_Code = 1;

    // XML Objects
    Button btButton;
    EditText nameInput,  ageInput, weightInput, heightInput, emailInput;
    RadioButton genderInputM, genderInputF, genderInputD;
    TextView connectionState;


    //Heartsensor Objects
    HeartSensorController permissionHeartSensorController;
    BluetoothDevice selectedHeartRateSensor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView():  ");

        View view = inflater.inflate(R.layout.settings_fragments, container, false);

        nameInput = (EditText) view.findViewById(R.id.nameInput);
        ageInput = (EditText) view.findViewById(R.id.ageInput);
        weightInput = (EditText) view.findViewById(R.id.weightInput);
        heightInput = (EditText) view.findViewById(R.id.heightInput);
        emailInput = (EditText) view.findViewById(R.id.emailInput);
        connectionState = (TextView) view.findViewById(R.id.connectionState);
        genderInputM = (RadioButton) view.findViewById(R.id.genderM);
        genderInputF = (RadioButton) view.findViewById(R.id.genderF);
        genderInputD = (RadioButton) view.findViewById(R.id.genderD);

        btButton = (Button) view.findViewById(R.id.btButton);
        btButton.setOnClickListener(this);

        loadUserFromPref();


        if (((MainActivity) getActivity()).getIsConnected()) {
            connectionState.setText("Verbunden");
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btButton:
                Log.v(TAG, "btButton clicked");
                startActivityForResult(new Intent(getActivity(), LeDeviceScanActivity.class), request_Code);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserFromPref();
    }

    @Override
    public void onStop() {
        super.onStop();
        saveFragmentPrefs();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveFragmentPrefs();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == request_Code) {
            if (resultCode == RESULT_OK) {
                ((MainActivity)getActivity()).setSelectedHeartRateSensor(data.getParcelableExtra(LeDeviceScanActivity.SELECTED_DEVICE));
                selectedHeartRateSensor = data.getParcelableExtra(LeDeviceScanActivity.SELECTED_DEVICE);

                ((MainActivity)getActivity()).startBluetoothFromFragment();
                connectionState.setText("Verbunden");

                ((MainActivity)getActivity()).setCurrentAdapter(selectedHeartRateSensor.getAddress());
                saveFragmentPrefs();
            } else {
                // Start simulation
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHeartSensorController.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void saveFragmentPrefs() {
        Log.v(TAG, "Saving contents");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NAME, nameInput.getText().toString());
        editor.putString(AGE, ageInput.getText().toString());
        editor.putString(WEIGHT, weightInput.getText().toString());
        editor.putString(HEIGHT, heightInput.getText().toString());
        editor.putString(EMAIL, emailInput.getText().toString());
        editor.putBoolean(MALE, genderInputM.isChecked());
        editor.putBoolean(DIVERS, genderInputD.isChecked());
        editor.putBoolean(FEMALE, genderInputF.isChecked());
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
        genderInputM.setChecked(sharedPreferences.getBoolean(MALE, false));
        genderInputF.setChecked(sharedPreferences.getBoolean(FEMALE, false));
        genderInputD.setChecked(sharedPreferences.getBoolean(DIVERS, false));
    }
}
