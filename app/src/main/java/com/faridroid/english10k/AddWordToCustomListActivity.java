package com.faridroid.english10k;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.faridroid.english10k.data.dto.CategoryDTO;
import com.faridroid.english10k.data.dto.CustomWordDTO;
import com.faridroid.english10k.view.adapter.CategoryAdapter;
import com.faridroid.english10k.view.adapter.CustomWordAdapter;
import com.faridroid.english10k.view.viewmodel.CategoryViewModel;
import com.faridroid.english10k.view.viewmodel.CustomListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AddWordToCustomListActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteCategory;
    private AutoCompleteTextView autoCompleteList;
    private EditText editTextWord;
    private EditText editTextTranslation;
    private Button buttonAddPair;
    private RecyclerView recyclerViewWordPairs;
    private CustomWordAdapter customWordAdapter;
    private List<CustomWordDTO> customWordList = new ArrayList<>();
    private String userId;
    private String lastCategoryId;
    private String lastListId;
    private CustomListViewModel customListModel;
    private CategoryViewModel categoryViewModel;
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word_to_custom_list);

        categoryViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(CategoryViewModel.class);

        autoCompleteCategory = findViewById(R.id.autoCompleteCategory);
        autoCompleteList = findViewById(R.id.autoCompleteList);
        editTextWord = findViewById(R.id.editTextWord);
        editTextTranslation = findViewById(R.id.editTextTranslation);
        buttonAddPair = findViewById(R.id.buttonAddPair);
        recyclerViewWordPairs = findViewById(R.id.recyclerViewWordPairs);
        customListModel = new ViewModelProvider(this).get(CustomListViewModel.class);

        //Get the user id
        userId = getIntent().getStringExtra("userId");
        // Verifica si la categoría "Mis listas" existe antes de observar
        categoryViewModel.getAllCategories(userId).observe(this, categoryDTOS -> {
            if (categoryDTOS == null || categoryDTOS.isEmpty()) {
                // Si la lista está vacía, inserta la categoría "Mis listas"
                insertDefaultCategory();
            } else {
                // Actualiza la vista si ya hay categorías disponibles
                updateCategoryView(categoryDTOS);
            }
        });

        autoCompleteCategory.setOnClickListener(view -> {
            if (!autoCompleteCategory.isPopupShowing()) {
                //autoCompleteCategory.showDropDown();
            }
        });


        autoCompleteCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No necesitas hacer nada aquí
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Actualiza el texto del input en el adaptador
                categoryAdapter.updateInputText(s.toString());

                // Forzar el filtrado para actualizar las sugerencias
                if (categoryAdapter != null) {
                    categoryAdapter.getFilter().filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        autoCompleteCategory.setThreshold(0);

        autoCompleteCategory.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = categoryAdapter.getItem(position);
            if (selectedItem != null && selectedItem.startsWith("Crear ")) {
                String newCategoryName = selectedItem.replace("Crear \"", "").replace("\"", "");
                createNewCategory(newCategoryName);
            }
        });

        autoCompleteCategory.setOnClickListener(view -> {
            // Fuerza el filtrado con una cadena vacía para mostrar todas las categorías
            if (!autoCompleteCategory.isPopupShowing()) {
                categoryAdapter.getFilter().filter(""); // Filtra con una cadena vacía para mostrar todas las categorías
                autoCompleteCategory.showDropDown();    // Muestra el dropdown
            }
        });
    }

    private void insertDefaultCategory() {
        CategoryDTO categoryDTO = new CategoryDTO(UUID.randomUUID().toString(), userId, "Mis listas", null);
        categoryViewModel.insertCategory(categoryDTO);
    }

    private void updateCategoryView(List<CategoryDTO> categoryDTOS) {
        List<String> categoryNames = categoryDTOS.stream().map(categoryDTO -> categoryDTO.getName()).collect(Collectors.toList());

        if (categoryAdapter == null) {
            categoryAdapter = new CategoryAdapter(this, categoryNames, autoCompleteCategory);
            autoCompleteCategory.setAdapter(categoryAdapter);
        } else {
            categoryAdapter.clear();
            categoryAdapter.addAll(categoryNames);
            categoryAdapter.notifyDataSetChanged();
        }
        categoryAdapter.setCategories(categoryDTOS);

        // Forzar la apertura del dropdown cuando se configuran las categorías
        autoCompleteCategory.post(() -> autoCompleteCategory.showDropDown());
    }



    private void createNewCategory(String categoryName) {
        CategoryDTO newCategory = new CategoryDTO(UUID.randomUUID().toString(), userId, categoryName, null);
        categoryViewModel.insertCategory(newCategory);
        // Actualiza la vista para reflejar la nueva categoría inmediatamente
        List<CategoryDTO> updatedCategories = new ArrayList<>(categoryAdapter.getCategories());
        updatedCategories.add(newCategory);
        updateCategoryView(updatedCategories);
    }
}
