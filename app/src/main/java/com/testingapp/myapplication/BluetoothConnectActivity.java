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
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BluetoothConnectActivity extends AppCompatActivity {
    BluetoothGatt bluetoothGatt;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice device;
    String adress;
    TextView txt;
    BluetoothGattService services;
    List<BluetoothGattCharacteristic> characteristics;
    ExpandableListView listView ;
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

        listView = findViewById(R.id.services);

    }
    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
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
                for(BluetoothGattService service: gatt.getServices()) {
                    if (!service.getUuid().toString().startsWith("000018"))
                    {   services = gatt.getService(service.getUuid());
                        characteristics  = services.getCharacteristics();
                        Log.d("zxc","SERVICE UUID " + services.getUuid());
                        for (BluetoothGattCharacteristic characteristic: characteristics){
                            Log.d("zxc","CUSTOM " + characteristic.getUuid().toString());
                        }
                    }
                }
            }
        }
        @Override
        public void onCharacteristicRead(@NonNull BluetoothGatt gatt, @NonNull BluetoothGattCharacteristic characteristic, @NonNull byte[] value, int status) {
            super.onCharacteristicRead(gatt, characteristic, value, status);

            byte[] value1 = characteristic.getValue();
            String test = new String(value1, StandardCharsets.UTF_8);
            Log.i("Bluetooth", test);
            Intent intent = new Intent(BluetoothConnectActivity.this,GetActivity.class);
            intent.putExtra("ip",test);
            startActivity(intent);
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
    public void click(View view) {
        bluetoothGatt = device.connectGatt(this,false,gattCallback,BluetoothDevice.TRANSPORT_LE);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetoothGatt.disconnect();
    }
    public void asdasd(View view) {
        String value = "MGTS_GPON_852A,Hesoyam99";
        BluetoothGattCharacteristic characteristic = characteristics.get(0);
        characteristic.setValue(value);
        if (bluetoothGatt.writeCharacteristic(characteristic)) {
            Log.i("Bluetooth", "Write initiated");
        } else {
            Log.i("Bluetooth", "Write operation could not be initiated");
        }
    }
    public void zxczxc(View view) {
        BluetoothGattCharacteristic characteristic = characteristics.get(0);
        if (bluetoothGatt.readCharacteristic(characteristic)) {
            Log.i("Bluetooth", "Read initiated");
        } else {
            Log.i("Bluetooth", "Read operation could not be initiated");
        }

    }
}
