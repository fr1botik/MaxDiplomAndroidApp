package com.testingapp.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DialogSettingsFragment extends DialogFragment{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settings_activity,container,false);
        Button btn_WiFi = view.findViewById(R.id.WiFi);
        Button btn_info = view.findViewById(R.id.Base_Info);

        btn_WiFi.setOnClickListener(v -> {
            if (getActivity() instanceof OnDataChangeListener) {
                ((OnDataChangeListener) getActivity()).onDataChanged(0);
            }

        });
        btn_info.setOnClickListener(v -> {
            if (getActivity() instanceof OnDataChangeListener) {
                ((OnDataChangeListener) getActivity()).onDataChanged(1);
            }
        });

        return view;

    }

    public interface OnDataChangeListener {
        void onDataChanged(int setting);
    }


}
