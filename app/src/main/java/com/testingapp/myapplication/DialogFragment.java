package com.testingapp.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

public class DialogFragment extends androidx.fragment.app.DialogFragment {
    final String[] working = {"работа1", "работа2", "работа3"};
    boolean[] checkedItemsArray = {false, false, false};

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {

        loadArray(getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Выберите котов")
                .setMultiChoiceItems(working, checkedItemsArray,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which, boolean isChecked) {
                                checkedItemsArray[which] = isChecked;
                            }
                        })
                .setPositiveButton("Готово",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                saveArray(checkedItemsArray,"checkedItemsArray",getContext());
                                dialog.cancel();
                            }
                        })

                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });

        return builder.create();
    }

    public boolean saveArray(boolean[] array, String arrayName, Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        SharedPreferences.Editor editor = prefs.edit();
        for(int i=0;i<array.length;i++)
            editor.putBoolean(arrayName + "_" + i, array[i]);
        return editor.commit();
    }

    public void loadArray(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);
        for(int i=0;i<checkedItemsArray.length;i++)
            checkedItemsArray[i] = prefs.getBoolean("checkedItemsArray" + "_" + i, false);
    }
}
