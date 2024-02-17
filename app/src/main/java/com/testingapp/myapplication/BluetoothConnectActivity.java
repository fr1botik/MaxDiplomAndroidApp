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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class BluetoothConnectActivity extends AppCompatActivity {
    BluetoothGatt bluetoothGatt;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice device;
    String adress;
    TextView txt;
    BluetoothGattService services;
    List<BluetoothGattCharacteristic> characteristics;
    Button btn,btn2;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_connect_activity);
        btn = findViewById(R.id.bta);
        btn2 = findViewById(R.id.button4);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        adress = getIntent().getStringExtra("device");
        device = bluetoothAdapter.getRemoteDevice(adress);
        bluetoothGatt = device.connectGatt(this,false,gattCallback,BluetoothDevice.TRANSPORT_LE);

        txt = findViewById(R.id.info);

        txt.setText(device.getAddress() + "\n" + device.getName());
    }
    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                            gatt.discoverServices();

                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    txt.setText("Подключитесь заново к устройству");
                    break;
            }

        }
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            btn2.setClickable(true);
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
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);

            byte[] value1 = characteristic.getValue();
            String test = new String(value1, StandardCharsets.UTF_8);
            Log.i("pppqqww", test);

            if(!test.equals("boob"))
            {
                SQL_Class sqlClass = new SQL_Class();
                if(sqlClass.SQL_connect()) {
                   int a = sqlClass.sql_device(device.getAddress());
                   Intent intent = new Intent(BluetoothConnectActivity.this, GetActivity.class);
                   intent.putExtra("ip", test);
                   intent.putExtra("id_device", a);
                   startActivity(intent);
                }
            }
            else{
                btn.setVisibility(View.VISIBLE);
            }
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetoothGatt.disconnect();
    }
    @SuppressLint("MissingPermission")
    public void Connect_WiFi(View view) {
        try {
            String value = "MGTS_GPON_0134,EFY35V9T";
            BluetoothGattCharacteristic characteristic = characteristics.get(0);
            characteristic.setValue(value);
            bluetoothGatt.writeCharacteristic(characteristic);
        }
        catch (Exception e){
        }

    }
    @SuppressLint("MissingPermission")
    public void Get_Ip(View view) {
        BluetoothGattCharacteristic characteristic = characteristics.get(1);
        bluetoothGatt.readCharacteristic(characteristic);
    }
}
