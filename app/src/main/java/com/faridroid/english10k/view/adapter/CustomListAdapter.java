package com.faridroid.english10k.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.faridroid.english10k.data.dto.CustomListDTO;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter<String> implements Filterable {

    private List<CustomListDTO> originalCategories;
    private List<String> filteredCategoryNames;


    public CustomListAdapter(Context context, List<String> categoryNames, List<CustomListDTO> originalCategories) {
        super(context, android.R.layout.simple_dropdown_item_1line, categoryNames);
        this.filteredCategoryNames = new ArrayList<>(categoryNames);
        this.originalCategories = originalCategories;

    }

    @Override
    public int getCount() {
        // Retorna el tamaño de la lista filtrada
        return filteredCategoryNames.size();
    }

    @Override
    public String getItem(int position) {
        return filteredCategoryNames.get(position);
    }

    public CustomListDTO getItemByName(String name) {
        for (CustomListDTO category : originalCategories) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }
        TextView textView = convertView.findViewById(android.R.id.text1);
        String categoryName = getItem(position);
        textView.setText(categoryName);
        return convertView;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence inputFilter) {
                FilterResults results = new FilterResults();
                List<String> suggestions = new ArrayList<>();

                if (inputFilter == null || inputFilter.length() == 0) {
                    suggestions.addAll(getOriginalCategoryNames());
                } else {
                    String filterPattern = inputFilter.toString().toLowerCase().trim();
                    for (CustomListDTO category : originalCategories) {
                        if (category.getName().toLowerCase().contains(filterPattern)) {
                            suggestions.add(category.getName());
                        }
                    }
                    // Si no hay coincidencias exactas, agrega el ítem "Crear [nombre]" al principio
                    if (isNewCategory(inputFilter.toString())) {
                        suggestions.add(0, "Crear \"" + inputFilter.toString() + "\"");
                    }
                }

                results.values = suggestions;
                results.count = suggestions.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredCategoryNames.clear();
                if (results != null && results.values != null) {
                    filteredCategoryNames.addAll((List<String>) results.values);
                }
                notifyDataSetChanged();
            }

            private List<String> getOriginalCategoryNames() {
                List<String> categoryNames = new ArrayList<>();
                for (CustomListDTO category : originalCategories) {
                    categoryNames.add(category.getName());
                }
                return categoryNames;
            }

            private boolean isNewCategory(String categoryName) {
                for (CustomListDTO category : originalCategories) {
                    if (category.getName().equals(categoryName.trim())) {
                        return false;
                    }
                }
                return true;
            }
        };
    }


    public void setCategories(List<CustomListDTO> categoryDTOS) {
        this.originalCategories = categoryDTOS;
        filteredCategoryNames.clear();
        for (CustomListDTO categoryDTO : categoryDTOS) {
            filteredCategoryNames.add(categoryDTO.getName());
        }
        notifyDataSetChanged();
    }

    public List<CustomListDTO> getCategories() {
        return originalCategories;
    }

    public String getCustomListId(int position) {
        return originalCategories.get(position).getId();
    }
}
