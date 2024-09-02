package com.faridroid.english10k.view.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.AutoCompleteTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.faridroid.english10k.data.dto.CategoryDTO;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends ArrayAdapter<String> implements Filterable {

    private List<CategoryDTO> originalCategories;
    private List<String> filteredCategoryNames;
    private AutoCompleteTextView autoCompleteCategory;
    private String inputText = "";

    public CategoryAdapter(Context context, List<String> categoryNames, AutoCompleteTextView autoCompleteCategory) {
        super(context, android.R.layout.simple_dropdown_item_1line, categoryNames);
        this.filteredCategoryNames = new ArrayList<>(categoryNames);
        this.originalCategories = new ArrayList<>(categoryNames.size());
        for (String name : categoryNames) {
            originalCategories.add(new CategoryDTO(null, null, name, null));
        }
        this.autoCompleteCategory = autoCompleteCategory;
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
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<String> suggestions = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    suggestions.addAll(getOriginalCategoryNames());
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (CategoryDTO category : originalCategories) {
                        if (category.getName().toLowerCase().contains(filterPattern)) {
                            suggestions.add(category.getName());
                        }
                    }
                    // Si no hay coincidencias exactas, agrega el ítem "Crear [nombre]" al principio
                    if (!suggestions.isEmpty()) {
                        suggestions.add(0, "Crear \"" + constraint.toString() + "\"");
                    } else {
                        // Si no hay coincidencias, simplemente agrega el ítem de creación
                        suggestions.add("Crear \"" + constraint.toString() + "\"");
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
                for (CategoryDTO category : originalCategories) {
                    categoryNames.add(category.getName());
                }
                return categoryNames;
            }
        };
    }

    public void updateInputText(String inputText) {
        this.inputText = inputText;
    }

    public void setCategories(List<CategoryDTO> categoryDTOS) {
        this.originalCategories = categoryDTOS;
        filteredCategoryNames.clear();
        for (CategoryDTO categoryDTO : categoryDTOS) {
            filteredCategoryNames.add(categoryDTO.getName());
        }
        notifyDataSetChanged();
    }

    public List<CategoryDTO> getCategories() {
        return originalCategories;
    }
}
