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

        public AdapterConnect(Context context,List<BluetoothGattService> list) {
        super();
        inflater = LayoutInflater.from(context);
        service = list;

    }
    @Override
    public int getGroupCount() {
        return service.size();
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        return service.get(groupPosition).getCharacteristics().size();
    }
    @Override
    public Object getGroup(int groupPosition) {
        return service.get(groupPosition);
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return service.get(groupPosition).getCharacteristics();
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
