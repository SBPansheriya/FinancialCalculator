package com.kmsoft.financialcalculator.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kmsoft.financialcalculator.R;

public class SpinnerAdapter extends ArrayAdapter<String> {
    Context c;
    String[] names;
    int[] images;
    LayoutInflater layoutInflater;

    public SpinnerAdapter(Context c, String[] names, int[] images) {
        super(c, R.layout.simple_spinner_dropdown_item2, names);
        this.c = c;
        this.names = names;
        this.images = images;
        layoutInflater = LayoutInflater.from(c);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") View row = layoutInflater.inflate(R.layout.simple_spinner_dropdown_item2, null, false);

        TextView name = row.findViewById(R.id.name);
        ImageView flag = row.findViewById(R.id.flag);

        name.setText(names[position]);
        flag.setBackgroundResource(images[position]);
        return row;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = layoutInflater.inflate(R.layout.simple_spinner_dropdown_item2, parent, false);

        TextView name = convertView.findViewById(R.id.name);
        ImageView flag = convertView.findViewById(R.id.flag);

        name.setText(names[position]);
        flag.setBackgroundResource(images[position]);
        return convertView;
    }
}
