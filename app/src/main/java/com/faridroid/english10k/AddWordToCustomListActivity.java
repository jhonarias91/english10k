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

import com.faridroid.english10k.data.dto.CustomListDTO;
import com.faridroid.english10k.data.dto.CustomWordDTO;
import com.faridroid.english10k.view.adapter.CustomListAdapter2;
import com.faridroid.english10k.view.adapter.CustomWordAdapter;
import com.faridroid.english10k.view.viewmodel.CustomListViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AddWordToCustomListActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteCategory;
    private EditText editTextWord;
    private EditText editTextTranslation;
    private Button buttonAddPair;
    private RecyclerView recyclerViewWordPairs;
    private CustomWordAdapter customWordAdapter;
    private List<CustomWordDTO> customWordList = new ArrayList<>();
    private String userId;
    private String currentCategoryId;
    private String lastListId;
    private CustomListViewModel customListModel;
    //private CustomListAdapter customListAdapter;
    private CustomListAdapter2 customListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word_to_custom_list);

        // Inicializa los ViewModels
        customListModel = new ViewModelProvider(this).get(CustomListViewModel.class);

        // Enlaza los componentes de la interfaz
        autoCompleteCategory = findViewById(R.id.autoCompleteCategory);
        editTextWord = findViewById(R.id.editTextWord);
        editTextTranslation = findViewById(R.id.editTextTranslation);
        buttonAddPair = findViewById(R.id.buttonAddPair);
        recyclerViewWordPairs = findViewById(R.id.recyclerViewWordPairs);

        userId = getIntent().getStringExtra("userId");
        currentCategoryId = getIntent().getStringExtra("categoryId");

        // Observa la categoría por defecto
        customListModel.getListsByCategoryId(currentCategoryId).observe(this, customList -> {
            if (customList == null || customList.isEmpty()) {
                createNewList("Mis palabras"); // Crea la categoría por defecto si no existe
            }
            updateCustomListView(customList);
        });

        // Maneja el filtrado para el AutoCompleteTextView de categoría
        autoCompleteCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (customListAdapter != null) {
                    customListAdapter.getFilter().filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        autoCompleteCategory.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && !autoCompleteCategory.isPopupShowing() && customListAdapter != null) {
                customListAdapter.getFilter().filter("");
                autoCompleteCategory.showDropDown();
            }
        });

        autoCompleteCategory.setThreshold(1);

        // Maneja la selección de un ítem en el dropdown
        autoCompleteCategory.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = customListAdapter.getItem(position);
            if (selectedItem != null && selectedItem.startsWith("Crear ")) {
                String newCustomListName = selectedItem.replace("Crear \"", "").replace("\"", "");
                autoCompleteCategory.setText(newCustomListName);
                createNewList(newCustomListName); // Crea una nueva lista si se selecciona la opción de creación
            }
        });
    }

    // Actualiza la vista de listas personalizadas basadas en la categoría seleccionada
    private void updateCustomListView(List<CustomListDTO> customListDTOS) {
        List<String> names = customListDTOS.stream().map(customListDTO -> customListDTO.getName()).collect(Collectors.toList());
        if (customListAdapter == null) {
            customListAdapter = new CustomListAdapter2(this, names, autoCompleteCategory);
            autoCompleteCategory.setAdapter(customListAdapter);
        } else {
            customListAdapter.setCategories(customListDTOS);
        }
        autoCompleteCategory.post(() -> autoCompleteCategory.showDropDown());
    }

    // Crea una nueva lista personalizada
    private void createNewList(String newCustomListName) {

         CustomListDTO newCustomList = new CustomListDTO(UUID.randomUUID().toString(), currentCategoryId, newCustomListName, null);
        customListModel.insertCustomList(newCustomList);
        // Actualiza la vista con la nueva lista
    }
}
