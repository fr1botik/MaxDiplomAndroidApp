package com.testingapp.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataPickerFragment extends DialogFragment {

    @Override
    public DatePickerDialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext());
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {

            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
            Log.d("DATE", sdf.format(calendar.getTime()));

        });

        return datePickerDialog;
    }
    public interface getDataInterface{

        void getData(String date);

    }
}
