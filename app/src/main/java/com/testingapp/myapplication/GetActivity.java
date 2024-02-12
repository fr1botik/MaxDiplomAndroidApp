package com.testingapp.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class GetActivity extends AppCompatActivity {

    //Объявление переменных
    OkHttpClient client = new OkHttpClient();
    List<String> list_temp1,list_temp2,list_magnet1,list_magnet2,list_vibro1,list_vibro2;
    String[] mass;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    Date date;
    Spinner spinner;
    String value,temp,magnet,addres;

    //Инициализация приложения
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_activity);
        mass = new String[]{"temp", "magnet", "vibro"};
        Date date = new Date();
        System.out.println(date);
        spinner = findViewById(R.id.spinner);
        addres = "http://" + getIntent().getStringExtra("ip");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mass);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                value = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //GET-запрос на получение данных температуры
    private void Get_temp(){

        list_temp1 = new ArrayList<>();
        list_temp2 = new ArrayList<>();

        Request request = new Request.Builder()
                .url(addres+"temp")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {

                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Запрос к серверу не был успешен: " +
                                response.code() + " " + response.message());
                    }
                    temp = responseBody.string();
                    String[]mass = temp.split("\\n");
                    for(int i = 0;i< mass.length;i++){
                        String[]mass1 = mass[i].split(",");
                        list_temp1.add(mass1[1]);
                        list_temp2.add(mass1[2]);
                    }
                    for (int j= 0;j<list_temp1.size();j++){
                        Log.println(Log.INFO,"Date and temperature",list_temp1.get(j)+"   "+ list_temp2.get(j));

                    }
                }
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.println(Log.ERROR,"FAIL",e.getMessage());
            }
        });
    }
    //GET-запрос на получение данных магнитного поля
    private void Get_magnet(){

        list_magnet1 = new ArrayList<>();
        list_magnet2 = new ArrayList<>();

        Request request = new Request.Builder()
                .url(addres+ "magnet")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {

                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("Запрос к серверу не был успешен: " +
                                response.code() + " " + response.message());
                    }
                    magnet = responseBody.string();
                    String[]mass = magnet.split("\\n");
                    for(int i = 0;i< mass.length;i++){
                        String[]mass1 = mass[i].split(",");
                        list_magnet1.add(mass1[1]);
                        list_magnet2.add(mass1[2]);
                    }
                    for (int j= 0;j<list_magnet1.size();j++){
                        Log.println(Log.INFO,"Date and magnet field",list_magnet1.get(j)+"   "+ list_magnet2.get(j));

                    }

                }
            }

            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.println(Log.ERROR,"FAIL",e.getMessage());
            }
        });
    }
    //Вывод графика с полученными данными
    private void Set_grapsh(List<String> list,List<String> list1){

        long[] xValues = new long[list.size()] ;
        double[] yValues =new double[list1.size()];

        for (int i=0;i<list1.size();i++){
            try {
                date = sdf.parse(list.get(i));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            long timestamp = date.getTime();
            xValues[i]= timestamp;
            yValues[i]=Double.parseDouble(list1.get(i));
        }
        ArrayList<Entry> entries = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            entries.add(new Entry((float) xValues[i],(float) yValues[i]));
        }
        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        LineData lineData = new LineData(dataSet);
        LineChart chart = findViewById(R.id.chart);
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getLegend().setEnabled(true);
        YAxis yAxis1 = chart.getAxisRight();
        yAxis1.setEnabled(false);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return sdf.format(new Date((long) value));

            }
        });
        chart.setData(lineData);
        chart.invalidate();
    }
//OnClick кнопки для получения данных
    public void Get_data(View view) {
        Get_temp();
        Get_magnet();

    }
    //OnClick кнопки для вывода данных на график

    public void Get_data1(View view) {

        switch (value){
            case "temp":
                    Set_grapsh(list_temp1,list_temp2);
                    break;
            case "magnet":
                Set_grapsh(list_magnet1,list_magnet2);
                break;
            default:
                break;
        }
    }

    public void open_dialog(View view) {

        DialogFragment dialogFragment = new DialogFragment();
        dialogFragment.show(getSupportFragmentManager(),"tag");


    }

}