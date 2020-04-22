package com.mikonski.quotes.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mikonski.quotes.R;

public class CustomAdapter extends BaseAdapter {
    Context context;
    String [] type;
    LayoutInflater lf;

    public CustomAdapter(Context context, String[] type) {
        this.context = context;
        this.type = type;
       lf = (LayoutInflater.from(context)) ;
    }


    @Override
    public int getCount() {
        return type.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       @SuppressLint("ViewHolder") View view = lf.inflate(R.layout.spinner_layout,null);
        TextView type1 = view.findViewById(R.id.text_spinner);
        type1.setText(type[position]);


       return view;
    }
}
