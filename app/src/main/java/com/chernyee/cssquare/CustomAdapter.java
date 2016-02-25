package com.chernyee.cssquare;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Issac on 2/24/2016.
 */
public class CustomAdapter extends ArrayAdapter<List<String>> {

    public CustomAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public CustomAdapter(Context context, int resource, List<List<String>> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi= LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_item, null);
        }

        List<String> p = getItem(position);



        if (p != null && p.size() > 0) {
            TextView tt1 = (TextView) v.findViewById(R.id.item_title);
            TextView tt2 = (TextView) v.findViewById(R.id.item_tag);
            TextView tt3 = (TextView) v.findViewById(R.id.item_difficulty);

            if (tt1 != null) {
                tt1.setText(p.get(1));
            }

            if (tt2 != null) {
                tt2.setText(p.get(6));
            }

            if (tt3 != null) {
                tt3.setText(p.get(8));
            }
        }

        return v;
    }

}