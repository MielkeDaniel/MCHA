package com.example.androidstudio.FittnessApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidstudio.FittnessApp.ui.main.Settings.SettingsFragment;

import de.hsfl.tjwa.blheartrateconnection.HeartSensorController;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private HeartSensorController heartSensorController;
    private String currentAdapter = "";
    private BluetoothDevice selectedHeartRateSensor;
    private BluetoothManager bluetoothManager;
    private boolean isConnected = false;
    private static String PREVMACADRESS = "PREVMACADRESS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        this.heartSensorController = new HeartSensorController(this);
        bluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, 1);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 1);
            }
        }

        loadFromPrefs();
        if (this.currentAdapter.length() > 0) {
            Log.v(TAG, "STARTING BLUETOOTH");
            selectedHeartRateSensor = bluetoothManager.getAdapter() .getRemoteDevice(currentAdapter);
            this.heartSensorController.startBluetooth(selectedHeartRateSensor);
            this.isConnected = true;

            if (heartSensorController.getHeartRate().getValue() == null) {
                this.heartSensorController.stopAll();
                    this.heartSensorController.startSimulation(1000);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        saveInPref();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveInPref();
    }

    private void saveInPref() {
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREVMACADRESS, currentAdapter);
        editor.apply();
    }

    private void loadFromPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE);
        currentAdapter = sharedPreferences.getString(PREVMACADRESS, "");
    }


    public void setSelectedHeartRateSensor(BluetoothDevice selectedHeartRateSensor) {
        this.selectedHeartRateSensor = selectedHeartRateSensor;
    }

    public void setCurrentAdapter(String currentAdapter) {
        this.currentAdapter = currentAdapter;
    }

    public void startBluetoothFromFragment() {
        Log.v(TAG, "STARTING BLUETOOTH");
        this.heartSensorController.startBluetooth(selectedHeartRateSensor);
        this.isConnected = true;
    }

    public HeartSensorController getHeartSensorController() {
        return this.heartSensorController;
    }

    public boolean getIsConnected() {
        return this.isConnected;
    }
}