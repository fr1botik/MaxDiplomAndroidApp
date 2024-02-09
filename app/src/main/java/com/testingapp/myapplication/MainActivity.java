package com.testingapp.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    BluetoothManager manager;
    BluetoothAdapter bluetoothAdapter;
    BluetoothLeScanner scanner;
    BluetoothGatt bluetoothGatt;
    List<BluetoothDevice> list;
    ListView listView;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permission();
        list = new ArrayList<>();
        manager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = manager.getAdapter();
        scanner = bluetoothAdapter.getBluetoothLeScanner();

        listView = findViewById(R.id.listview);
        adapter = new Adapter(this,R.layout.list_item,list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = list.get(position);
            }
        });
    }
    private void permission(){

        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        if (checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.BLUETOOTH_SCAN}, 1);
        }
        if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, 1);
        }
    }
    private ScanCallback scanCallback = new ScanCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice device = result.getDevice();

            if(!list.contains(device)){
                list.add(device);
            }
            adapter.notifyDataSetChanged();
        }
        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }
        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
        }
    };
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
    @SuppressLint("MissingPermission")
    public void click(View view){
        list.clear();
        adapter.notifyDataSetChanged();

        scanner.startScan(scanCallback);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scanner.stopScan(scanCallback);
                for(BluetoothDevice device1:list) {
                    Log.d("FAS", device1.getAddress().toString() + "   " + device1.getName());
                }
            }
        }, 5000);

    }
    @SuppressLint("MissingPermission")
    private void stropscan(){

        scanner.stopScan(scanCallback);
    }
}