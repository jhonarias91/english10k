package com.faridroid.english10k;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faridroid.english10k.data.dto.CustomListDTO;
import com.faridroid.english10k.data.dto.CustomWordDTO;
import com.faridroid.english10k.data.dto.interfaces.WordInterface;
import com.faridroid.english10k.view.adapter.CustomListAdapter;
import com.faridroid.english10k.view.adapter.CustomWordAdapter;
import com.faridroid.english10k.view.viewmodel.CustomListViewModel;
import com.faridroid.english10k.view.viewmodel.CustomWordViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;
import java.util.stream.Collectors;

public class AddWordToCustomListActivity extends AppCompatActivity implements CustomWordAdapter.OnCustomWordDeleteListener {

    private AutoCompleteTextView autoCompleteCategory;
    private EditText editTextWord;
    private EditText editTextTranslation;
    private Button buttonAddPair;
    private RecyclerView recyclerViewWordPairs;
    private CustomWordAdapter customWordAdapter;
    private List<WordInterface> customWordList = new ArrayList<>();
    private String userId;
    private String currentCategoryId;
    private String currentListId;
    private CustomListViewModel customListModel;
    private CustomWordViewModel customWordViewModel;
    private CustomListAdapter customListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word_to_custom_list);

        // Inicializa los ViewModels
        customListModel = new ViewModelProvider(this).get(CustomListViewModel.class);

        customWordViewModel = new ViewModelProvider(this).get(CustomWordViewModel.class);

        // Enlaza los componentes de la interfaz
        autoCompleteCategory = findViewById(R.id.autoCompleteCategory);
        editTextWord = findViewById(R.id.editTextWord);
        editTextTranslation = findViewById(R.id.editTextTranslation);
        buttonAddPair = findViewById(R.id.buttonPlayList);
        recyclerViewWordPairs = findViewById(R.id.recyclerViewWordPairs);
        //linearLayout to show the list of words
        recyclerViewWordPairs.setLayoutManager(new GridLayoutManager(this, 2));


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

        autoCompleteCategory.setThreshold(1);
        autoCompleteCategory.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && customListAdapter != null) {
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
                Toast.makeText(this, newCustomListName + " creada", Toast.LENGTH_SHORT).show();
            } else {
                CustomListDTO itemByName = customListAdapter.getItemByName(selectedItem);
                if (itemByName != null) {
                    this.currentListId = itemByName.getId();
                    customWordViewModel.getCustomWordsByList(currentListId).observe(this, customWords -> {
                        if (customWords != null) {
                            customWordList.clear();  // Limpia la lista existente
                            customWordList.addAll(customWords);  // Añade las nuevas palabras
                            if (customWordAdapter == null) {
                                customWordAdapter = new CustomWordAdapter(customWordList, this);
                                recyclerViewWordPairs.setAdapter(customWordAdapter);
                            } else {
                                customWordAdapter.notifyDataSetChanged();  // Notifica los cambios al adaptador
                            }
                        }
                    });
                }
            }
        });

        //Try to fix show when click and empty
        autoCompleteCategory.setOnClickListener(v -> {
            if (!autoCompleteCategory.isPopupShowing() && customListAdapter != null) {
                autoCompleteCategory.showDropDown();
            }
        });
        setBtnCreateWord();
    }

    private void setBtnCreateWord() {
        buttonAddPair.setOnClickListener(v -> {
            String word = editTextWord.getText().toString();
            String translation = editTextTranslation.getText().toString();
            if (word.isEmpty() || translation.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese una palabra y su traducción", Toast.LENGTH_SHORT).show();
                return;
            }
            CustomWordDTO customWord = new CustomWordDTO(UUID.randomUUID().toString(), currentListId, word, translation);
            customWordList.add(customWord);
            customWordViewModel.insertCustomWord(customWord);
            Toast.makeText(this, "Palabra añadida", Toast.LENGTH_SHORT).show();

            if (customWordAdapter == null) {
                customWordAdapter = new CustomWordAdapter(customWordList, this);
                recyclerViewWordPairs.setAdapter(customWordAdapter);
            } else {
                customWordAdapter.setCustomWordList(customWordList);
                customWordAdapter.notifyItemInserted(customWordList.size() - 1); // Notificar la adición del nuevo ítem
            }

            editTextWord.setText("");
            editTextTranslation.setText("");
            editTextWord.requestFocus();
        });
    }

    // Actualiza la vista de listas personalizadas basadas en la categoría seleccionada
    private void updateCustomListView(List<CustomListDTO> customListDTOS) {
        List<String> names = customListDTOS.stream().map(customListDTO -> customListDTO.getName()).collect(Collectors.toList());
        if (customListAdapter == null) {
            customListAdapter = new CustomListAdapter(this, names, customListDTOS);
            autoCompleteCategory.setAdapter(customListAdapter);
        } else {
            customListAdapter.setCategories(customListDTOS);
        }

        autoCompleteCategory.post(() -> {
            if (!autoCompleteCategory.isPopupShowing()) {
                autoCompleteCategory.showDropDown();
            }
        });
    }

    // Crea una nueva lista personalizada
    private void createNewList(String newCustomListName) {

        CustomListDTO newCustomList = new CustomListDTO(UUID.randomUUID().toString(), currentCategoryId, newCustomListName, null);
        customListModel.insertCustomList(newCustomList);
        this.currentListId = newCustomList.getId();
        // Actualiza la vista con la nueva lista
    }

    private Stack<WordInterface> deletedWordsStack = new Stack<>();
    private List<WordInterface> batchDeletedWords = new ArrayList<>();
    private Stack<Integer> deletedPositionsStack = new Stack<>();
    private Set<String> restoredWordsSet = new HashSet<>();
    private Snackbar currentSnackbar = null; // Variable para rastrear el Snackbar actual
    private boolean undoClicked = false; // Variable para indicar si se hizo clic en "Deshacer"

    @Override
    public void onDelete(String wordId, int position) {
        // Verifica que el índice sea válido antes de acceder a la lista
        if (position >= 0 && position < customWordList.size()) {
            // Elimina la palabra seleccionada
            WordInterface deletedWord = customWordList.get(position);
            deletedWordsStack.push(deletedWord);
            deletedPositionsStack.push(position);
            restoredWordsSet.remove(deletedWord.getId());

            customWordList.remove(position);
            customWordAdapter.setCustomWordList(customWordList);
            customWordAdapter.notifyItemRemoved(position);

            // Agrega la palabra eliminada a la lista acumulativa
            batchDeletedWords.add(deletedWord);

            // Si ya hay un Snackbar visible, actualizamos el mensaje en lugar de mostrar uno nuevo
            if (currentSnackbar != null) {
                // Actualiza el mensaje del Snackbar actual
                currentSnackbar.setText("Se eliminaron " + batchDeletedWords.size() + " palabras");
            } else {
                // Crea un nuevo Snackbar si no hay uno visible
                Snackbar snackbar = Snackbar.make(recyclerViewWordPairs, "Palabra eliminada", Snackbar.LENGTH_LONG);
                snackbar.setAction("Deshacer", v -> {
                    undoClicked = true;
                    // Restaurar las palabras eliminadas
                    while (!deletedWordsStack.isEmpty() && !deletedPositionsStack.isEmpty()) {
                        WordInterface restoredWord = deletedWordsStack.pop();
                        int originalPosition = deletedPositionsStack.pop();
                        int restoredPosition = Math.min(originalPosition, customWordList.size());

                        customWordList.add(restoredPosition, restoredWord);
                        customWordAdapter.setCustomWordList(customWordList);
                        customWordAdapter.notifyItemInserted(restoredPosition);
                        restoredWordsSet.add(restoredWord.getId());
                    }
                    // Limpiamos la lista de eliminadas acumuladas
                    batchDeletedWords.clear();
                });

                // Manejamos la visibilidad del Snackbar y las acciones
                snackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onShown(Snackbar sb) {
                        currentSnackbar = sb;
                        undoClicked = false;
                    }

                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (snackbar == currentSnackbar) {
                            currentSnackbar = null;
                        }

                        // Elimina permanentemente las palabras si el usuario no hizo clic en "Deshacer"
                        if (event != Snackbar.Callback.DISMISS_EVENT_ACTION && !undoClicked) {
                            for (WordInterface word : batchDeletedWords) {
                                customWordViewModel.deleteCustomWord(word.getId());
                            }
                            // Limpiar las pilas y la lista acumulativa
                            deletedWordsStack.clear();
                            deletedPositionsStack.clear();
                            batchDeletedWords.clear();
                        }
                    }
                });
                snackbar.show();
                currentSnackbar = snackbar;
            }
        } else {
            // Manejo de error para posición inválida
            Log.e("onDelete", "Índice fuera de rango: " + position);
        }
    }



}
