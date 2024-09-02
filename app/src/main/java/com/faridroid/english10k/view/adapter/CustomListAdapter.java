package com.faridroid.english10k.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.faridroid.english10k.data.dto.CustomListDTO;

import java.util.List;

public class CustomListAdapter extends ArrayAdapter<CustomListDTO> {

    public CustomListAdapter(Context context, List<CustomListDTO> categories) {
        super(context, android.R.layout.simple_dropdown_item_1line, categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Aquí personalizas cómo se muestran las categorías en la lista
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }
        TextView textView = convertView.findViewById(android.R.id.text1);
        CustomListDTO customList = getItem(position);
        textView.setText(customList.getName());

        return convertView;
    }
}
