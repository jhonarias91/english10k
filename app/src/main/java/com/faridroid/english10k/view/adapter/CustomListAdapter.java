package com.faridroid.english10k.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.faridroid.english10k.data.dto.CustomListDTO;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter<String> implements Filterable {

    private List<CustomListDTO> originalList;
    private List<String> filteredCategoryNames;
    private final AutoCompleteTextView autoCompleteCategory;

    public CustomListAdapter(Context context, List<String> categoryNames, List<CustomListDTO> originalCategories, AutoCompleteTextView autoCompleteCategory) {
        super(context, android.R.layout.simple_dropdown_item_1line, categoryNames);
        this.filteredCategoryNames = new ArrayList<>(categoryNames);
        this.originalList = new ArrayList<>(originalCategories);
        this.autoCompleteCategory = autoCompleteCategory;
    }

    @Override
    public int getCount() {
        return filteredCategoryNames.size();
    }

    @Override
    public String getItem(int position) {
        return filteredCategoryNames.get(position);
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

    @NonNull
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
                    for (CustomListDTO category : originalList) {
                        if (category.getName().toLowerCase().contains(filterPattern)) {
                            suggestions.add(category.getName());
                        }
                    }
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
                autoCompleteCategory.post(() -> autoCompleteCategory.showDropDown());
            }

            private List<String> getOriginalCategoryNames() {
                List<String> categoryNames = new ArrayList<>();
                for (CustomListDTO list : originalList) {
                    categoryNames.add(list.getName());
                }
                return categoryNames;
            }

            private boolean isNewCategory(String categoryName) {
                for (CustomListDTO list : originalList) {
                    if (list.getName().equalsIgnoreCase(categoryName.trim())) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    public void setCategories(List<CustomListDTO> customListDTOS) {
        this.originalList = customListDTOS;
        filteredCategoryNames.clear();
        for (CustomListDTO categoryDTO : customListDTOS) {
            filteredCategoryNames.add(categoryDTO.getName());
        }
        notifyDataSetChanged();
    }

    public List<CustomListDTO> getCategories() {
        return originalList;
    }
}
