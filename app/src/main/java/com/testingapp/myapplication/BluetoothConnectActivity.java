package com.testingapp.myapplication;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class BluetoothConnectActivity extends AppCompatActivity {
    BluetoothGatt bluetoothGatt;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice device;
    String adress;
    TextView txt;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_connect_activity);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        adress = getIntent().getStringExtra("device");
        device = bluetoothAdapter.getRemoteDevice(adress);
        Log.d("ZXC",device.getAddress() + "   " + device.getName());
        txt = findViewById(R.id.info);
        txt.setText(device.getAddress() + "\n" + device.getName());

    }
    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("Bluetooth", "Connected to GATT server.");
                    // Попробуйте запустить обнаружение служб
                    Log.i("Bluetooth", "Attempting to start service discovery:" +
                            gatt.discoverServices());
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.i("Bluetooth", "Disconnected from GATT server.");
                    break;
            }
        }
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // На этом этапе вы можете получить информацию о службах
                List<BluetoothGattService> services = gatt.getServices();
            } else {
                Log.w("Bluetooth", "onServicesDiscovered received: " + status);
            }

        }
        @Override
        public void onCharacteristicRead(@NonNull BluetoothGatt gatt, @NonNull BluetoothGattCharacteristic characteristic, @NonNull byte[] value, int status) {
            super.onCharacteristicRead(gatt, characteristic, value, status);
        }
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }
        @Override
        public void onCharacteristicChanged(@NonNull BluetoothGatt gatt, @NonNull BluetoothGattCharacteristic characteristic, @NonNull byte[] value) {
            super.onCharacteristicChanged(gatt, characteristic, value);
        }
    };
}
