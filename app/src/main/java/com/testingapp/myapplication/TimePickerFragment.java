package com.testingapp.myapplication;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.time_pick,container,false);
        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TimePicker startTimePicker = view.findViewById(R.id.start_time_picker);
        TimePicker endTimePicker = view.findViewById(R.id.end_time_picker);
        Button okButton = view.findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Обработка нажатия на кнопку OK
                // Получите выбранные значения времени начала и окончания
                int startHour = startTimePicker.getHour();
                int startMinute = startTimePicker.getMinute();
                int endHour = endTimePicker.getHour();
                int endMinute = endTimePicker.getMinute();

                // Пример вывода выбранного периода времени
                Log.d("TimePicker", "Выбранный период времени: " + startHour + ":" + startMinute + " - " + endHour + ":" + endMinute);

                dismiss(); // Закрыть диалоговое окно
            }
        });
    }
    public interface getTimeInterface{
        void getTime(String date_start,String date_end);

    }
}
