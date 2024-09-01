package com.faridroid.english10k;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faridroid.english10k.view.adapter.WordDTOAdapter;
import com.faridroid.english10k.viewmodel.dto.WordDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddWordToCustomListActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteCategory;
    private AutoCompleteTextView autoCompleteList;
    private EditText editTextWord;
    private EditText editTextTranslation;
    private Button buttonAddPair;
    private RecyclerView recyclerViewWordPairs;
    private WordDTOAdapter wordPairAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word_to_custom_list);

        autoCompleteCategory = findViewById(R.id.autoCompleteCategory);
        autoCompleteList = findViewById(R.id.autoCompleteList);
        editTextWord = findViewById(R.id.editTextWord);
        editTextTranslation = findViewById(R.id.editTextTranslation);
        buttonAddPair = findViewById(R.id.buttonAddPair);
        recyclerViewWordPairs = findViewById(R.id.recyclerViewWordPairs);

        // Configura el RecyclerView
        wordPairAdapter = new WordDTOAdapter(new ArrayList<>());
        recyclerViewWordPairs.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewWordPairs.setAdapter(wordPairAdapter);

        // Cargar categorías existentes (simulación)
        List<String> categories = loadCategories(); // Método que carga las categorías
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);
        autoCompleteCategory.setAdapter(categoryAdapter);

        autoCompleteCategory.setOnItemClickListener((parent, view, position, id) -> {
            String selectedCategory = categoryAdapter.getItem(position);
            loadListsForCategory(selectedCategory); // Método para cargar listas basadas en la categoría
        });

        autoCompleteCategory.setOnDismissListener(() -> {
            if (!categories.contains(autoCompleteCategory.getText().toString())) {
                // Crear nueva categoría
                createNewCategory(autoCompleteCategory.getText().toString());
            }
        });

        autoCompleteList.setOnDismissListener(() -> {
            if (!isListExisting(autoCompleteList.getText().toString())) {
                // Crear nueva lista
                createNewList(autoCompleteList.getText().toString());
            }
        });

        buttonAddPair.setOnClickListener(v -> {
            String word = editTextWord.getText().toString();
            String spanish = editTextTranslation.getText().toString();
            if (!word.isEmpty() && !spanish.isEmpty()) {
                wordPairAdapter.addWord(new WordDTO(word, spanish));
                editTextWord.setText("");
                editTextTranslation.setText("");
            } else {
                Toast.makeText(this, "Por favor, ingrese la palabra y su traducción", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<String> loadCategories() {
        // Simulación de carga de categorías desde la base de datos o API
        return Arrays.asList("Mis listas", "Songs", "Travel", "Work");
    }

    private void loadListsForCategory(String category) {
        // Simulación de carga de listas basadas en la categoría seleccionada
        List<String> lists = new ArrayList<>();
        if ("Mis listas".equals(category)) {
            lists = Arrays.asList("Lista 1", "Lista 2");
        } else if ("Songs".equals(category)) {
            lists = Arrays.asList("Pop Hits", "Classical Favorites");
        }
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, lists);
        autoCompleteList.setAdapter(listAdapter);
    }

    private boolean isListExisting(String listName) {
        // Lógica para verificar si la lista existe
        return true; // Simulación
    }

    private void createNewCategory(String categoryName) {
        // Lógica para crear una nueva categoría
        Toast.makeText(this, "Categoría '" + categoryName + "' creada", Toast.LENGTH_SHORT).show();
    }

    private void createNewList(String listName) {
        // Lógica para crear una nueva lista en la categoría seleccionada
        Toast.makeText(this, "Lista '" + listName + "' creada", Toast.LENGTH_SHORT).show();
    }
}
