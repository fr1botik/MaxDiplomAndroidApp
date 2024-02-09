package com.testingapp.myapplication;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
public class AdapterConnect extends BaseExpandableListAdapter {

    private List<BluetoothGattService> service;
    private LayoutInflater inflater;
    private  List<BluetoothGattCharacteristic> characteristics;

    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return service.get(groupPosition).getUuid();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return service.get(groupPosition).getCharacteristics().get(childPosition).getUuid();
    }

    public AdapterConnect(Context context, List<BluetoothGattService> list, List<BluetoothGattCharacteristic> list1) {
        super();
        inflater = LayoutInflater.from(context);
        service = list;
        characteristics = list1;

    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.services, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.txt);
        textView.setText(getGroup(groupPosition).toString());

        return convertView;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.services, null);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.txt);
        textView.setText(getChild(groupPosition, childPosition).toString());

        return convertView;
    }

}
