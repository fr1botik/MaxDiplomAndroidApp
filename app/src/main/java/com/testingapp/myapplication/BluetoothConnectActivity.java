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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.List;

public class BluetoothConnectActivity extends AppCompatActivity implements DialogSettingsFragment.OnDataChangeListener {
    BluetoothGatt bluetoothGatt;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice device;
    String adress;
    TextView txt;
    BluetoothGattService services;
    List<BluetoothGattCharacteristic> characteristics;
    Button btn_wifi,btn_check_wifi,btn_settings,btn_graphics,btn_send_base_info;
    LinearLayout base_info;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_connect_activity);
        init();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        adress = getIntent().getStringExtra("device");
        device = bluetoothAdapter.getRemoteDevice(adress);
        bluetoothGatt = device.connectGatt(this,false,gattCallback,BluetoothDevice.TRANSPORT_LE);

        txt = findViewById(R.id.info);

    }

    private void init(){

        btn_wifi = findViewById(R.id.Connect_WiFi);
        btn_check_wifi = findViewById(R.id.Check_WiFi_Connect);
        btn_send_base_info = findViewById(R.id.Send_Base_Info);
        btn_settings = findViewById(R.id.Settings);
        btn_graphics = findViewById(R.id.Graphics);
        base_info = findViewById(R.id.layout_base);


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
        @SuppressLint("MissingPermission")
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);

            if (status == BluetoothGatt.GATT_SUCCESS) {

                for(BluetoothGattService service: gatt.getServices()) {
                    if (!service.getUuid().toString().startsWith("000018"))
                    {   services = gatt.getService(service.getUuid());
                        characteristics  = services.getCharacteristics();
                    }
                }
                runOnUiThread(() -> {
                    btn_settings.setVisibility(View.VISIBLE);
                    btn_graphics.setVisibility(View.VISIBLE);
                    BluetoothGattCharacteristic characteristic = characteristics.get(2);
                    bluetoothGatt.readCharacteristic(characteristic);
                });
            }
        }
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);

            byte[] value1 = characteristic.getValue();
            String test = new String(value1, StandardCharsets.UTF_8);

            if(!test.equals("boob") && !characteristic.equals(characteristics.get(2)))
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
            else if(characteristic.equals(characteristics.get(2)))
            {
                String [] mass = test.split(",");
                txt.setText(MessageFormat.format("SN:{0}\tЦех:{1}\tЗавод:{2}", mass[0], mass[1], mass[2]));

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

        EditText login,password;
        login = findViewById(R.id.Login);
        password = findViewById(R.id.Password);
        if(!login.getText().equals("") && !password.getText().equals("") ) {
            String wifi = login.getText() + "," + password.getText();

            try {
                BluetoothGattCharacteristic characteristic = characteristics.get(0);
                characteristic.setValue(wifi);
                bluetoothGatt.writeCharacteristic(characteristic);
                LinearLayout wifi_conn = findViewById(R.id.wifi_conn);
                wifi_conn.setVisibility(View.GONE);
            } catch (Exception e) {
            }
        }
        else {
            Toast.makeText(BluetoothConnectActivity.this,"Введите название сети и пароль!",Toast.LENGTH_SHORT).show();
        }

    }
    @SuppressLint("MissingPermission")
    public void Get_Ip(View view) {
        BluetoothGattCharacteristic characteristic = characteristics.get(1);
        bluetoothGatt.readCharacteristic(characteristic);
    }

    public void show(View view) {
        LinearLayout wifi_conn = findViewById(R.id.wifi_conn);
        wifi_conn.setVisibility(View.VISIBLE);
    }

    @SuppressLint("MissingPermission")
    public void click(View view) {

    }

    @SuppressLint("MissingPermission")
    public void send_info(View view) {

        BluetoothGattCharacteristic characteristic = characteristics.get(2);
        characteristic.setValue("Значение1,Значение2,Значение3");
        bluetoothGatt.writeCharacteristic(characteristic);
    }

    @Override
    public void onDataChanged(int setting) {

        btn_settings.setVisibility(View.GONE);
        btn_graphics.setVisibility(View.VISIBLE);
        switch (setting){

            case 0:
                btn_wifi.setVisibility(View.VISIBLE);

                break;
            case 1:

                break;

        }
    }
}
