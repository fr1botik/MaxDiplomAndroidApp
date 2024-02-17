package com.testingapp.myapplication;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

public class SQL_Class {

    private static String ip = "192.168.5.67";
    private static String  port= "61344";
    private static String Clases = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "IoT_device";
    private static String user = "connect";
    private static  String password = "123456";
    private static  String url = "jdbc:jtds:sqlserver://"+ip+":"+port+":"+database;
    private Connection connection = null;

    public boolean SQL_connect(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        boolean connect = false;
        try {
            Class.forName(Clases);
            connection = DriverManager.getConnection(url,user,password);
            Log.d("SQL_CONNECT","CONNECTED");
            connect = true;
        } catch (ClassNotFoundException e) {
            Log.e("Class not FOund",e.getMessage());
        } catch (SQLException e) {
            Log.e("CXZCXCX",e.getMessage());
        }
        return connect;
    }
    public int sql_device(String mac){
        int a=1;
        if(connection!=null){
            try {
                String checkQuery = "use "+ database+" \n SELECT * FROM Devices WHERE MAC = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                checkStatement.setString(1, mac );
                ResultSet resultSet = checkStatement.executeQuery();

                if (!resultSet.next()) {
                    String insertQuery ="use "+ database+" \n INSERT INTO Devices (MAC,SN_engine) VALUES (?,54655)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                    insertStatement.setString(1, mac);
                    insertStatement.executeUpdate();

                    String query = "use "+ database+" \n SELECT * FROM Devices WHERE MAC = ?";
                    PreparedStatement Statement = connection.prepareStatement(query);
                    Statement.setString(1, mac );
                    ResultSet resultSet1 = checkStatement.executeQuery();
                    a = resultSet1.getInt(2);
                }
                else{
                    a = resultSet.getInt(1);
                    Log.i("MAC", String.valueOf(resultSet.getInt(1)));

                }
            } catch (SQLException e) {
                Log.e("852",e.getMessage());
            }
        }
        return a;
    }
    public void sql_temp(int id_device,String Data,String temperature,String time){

        if(connection!=null){
            try {
                String checkQuery = "use "+ database+" \n SELECT * FROM Temperature WHERE Time = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                checkStatement.setString(1, time);
                ResultSet resultSet = checkStatement.executeQuery();
                Log.i("bnjhbn","ASJDNASFKLA");

                if (!resultSet.next()) {
                    String insertQuery ="use "+ database+" \n INSERT INTO Temperature (Date,Time,Temperature,ID_device) VALUES (?,?,?,?)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                    insertStatement.setString(1, Data);
                    insertStatement.setString(2, time);
                    insertStatement.setString(3,temperature);
                    insertStatement.setInt(4,id_device);
                    insertStatement.executeUpdate();
                }
            } catch (SQLException e) {
                Log.e("852",e.getMessage());
            }
        }
    }
    public void sql_vibro(int id_device,String Data,String vibro,String time) {
        if(connection!=null){
            try {
                String checkQuery = "use "+ database+" \n SELECT * FROM Vibration WHERE Time = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                checkStatement.setString(1, time);
                ResultSet resultSet = checkStatement.executeQuery();
                Log.i("bnjhbn","ASJDNASFKLA");

                if (!resultSet.next()) {
                    String insertQuery ="use "+ database+" \n INSERT INTO Vibration (Date,Time,Vibration,ID_device) VALUES (?,?,?,?)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                    insertStatement.setString(1, Data);
                    insertStatement.setString(2, time);
                    insertStatement.setString(3,vibro);
                    insertStatement.setInt(4,id_device);
                    insertStatement.executeUpdate();
                }
            } catch (SQLException e) {
                Log.e("852",e.getMessage());
            }
        }
    }
    public void sql_magnetic(int id_device,String Data,String magnetic,String time){
        if(connection!=null){
            try {
                String checkQuery = "use "+ database+" \n SELECT * FROM Magnetic WHERE Time = ?";
                PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
                checkStatement.setString(1, time);
                ResultSet resultSet = checkStatement.executeQuery();
                Log.i("bnjhbn","ASJDNASFKLA");

                if (!resultSet.next()) {
                    String insertQuery ="use "+ database+" \n INSERT INTO Magnetic (Date,Time,Magnetic,ID_device) VALUES (?,?,?,?)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                    insertStatement.setString(1, Data);
                    insertStatement.setString(2, time);
                    insertStatement.setString(3,magnetic);
                    insertStatement.setInt(4,id_device);
                    insertStatement.executeUpdate();
                }
            } catch (SQLException e) {
                Log.e("852",e.getMessage());
            }
        }


    }
}