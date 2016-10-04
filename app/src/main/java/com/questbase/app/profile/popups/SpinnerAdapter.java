package com.questbase.app.profile.popups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.questbase.app.R;

public class SpinnerAdapter extends ArrayAdapter {

    String spinnerItems[];

    public SpinnerAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        spinnerItems = objects;
    }

    public View getCustomView(int position, View convertView,
                              ViewGroup parent) {
        View layout;
        if (position == 0) {
            layout = LayoutInflater.from(getContext()).inflate(R.layout.simple_spinner_item, parent, false);
        } else {
            layout = LayoutInflater.from(getContext()).inflate(R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView spinnerChoice = (TextView) layout
                .findViewById(R.id.spinner_choice);
        spinnerChoice.setText(spinnerItems[position]);
        return layout;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.simple_spinner_item, parent, false);
        TextView spinnerChoice = (TextView) layout
                .findViewById(R.id.spinner_choice);
        spinnerChoice.setText(spinnerItems[position]);
        return layout;
    }
}
