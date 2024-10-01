package com.faridroid.english10k;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faridroid.english10k.data.dto.CategoryDTO;
import com.faridroid.english10k.data.dto.CustomListDTO;
import com.faridroid.english10k.data.dto.UserDTO;
import com.faridroid.english10k.data.dto.interfaces.WordInterface;
import com.faridroid.english10k.view.adapter.CustomListPracticeAdapter;
import com.faridroid.english10k.view.adapter.CustomWordAdapterOnPractice;
import com.faridroid.english10k.view.adapter.OnLearnedWordClickListener;
import com.faridroid.english10k.view.viewmodel.CategoryViewModel;
import com.faridroid.english10k.view.viewmodel.CustomListViewModel;
import com.faridroid.english10k.view.viewmodel.CustomUserProgressViewModel;
import com.faridroid.english10k.view.viewmodel.CustomWordViewModel;
import com.faridroid.english10k.view.viewmodel.intf.OnLearnForgetCustomWordListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PracticeMyListActivity extends AppCompatActivity implements OnLearnForgetCustomWordListener, OnLearnedWordClickListener {

    private Spinner categorySpinnerPractice;

    private AutoCompleteTextView autoCompleteCategory;
    private Button buttonPlayList;
    private RecyclerView recyclerViewWordPairs;
    private CustomWordAdapterOnPractice customWordAdapter;
    private List<WordInterface> customWordList = new ArrayList<>();
    private UserDTO user;
    private String currentListId;
    private CustomListViewModel customListModel;
    private CategoryViewModel categoryViewModel;
    private CustomWordViewModel customWordViewModel;
    private CustomUserProgressViewModel customUserProgressViewModel;
    private CustomListPracticeAdapter customListAdapter;
    private SwitchCompat switchCompatLearned;
    private TextView textViewWordPairsPractice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_list);

        initializeViewModels();
        bindViews();
        retrieveUserFromIntent();
        observeCategories();
        setupAutoCompleteCategory();
        setupButtonPlayList();
        setupSwitchLearned();
    }

    /**
     * Inicializa los ViewModels necesarios para la actividad.
     */
    private void initializeViewModels() {
        customListModel = new ViewModelProvider(this).get(CustomListViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        customWordViewModel = new ViewModelProvider(this).get(CustomWordViewModel.class);
        customUserProgressViewModel = new ViewModelProvider(this).get(CustomUserProgressViewModel.class);
    }

    /**
     * Enlaza los componentes de la interfaz de usuario con sus respectivas vistas.
     */
    private void bindViews() {
        autoCompleteCategory = findViewById(R.id.autoCompleteCustomListPractice);
        buttonPlayList = findViewById(R.id.buttonPlayListPractice);
        recyclerViewWordPairs = findViewById(R.id.recyclerViewWordPairsPractice);
        categorySpinnerPractice = findViewById(R.id.categorySpinnerPractice);
        switchCompatLearned = findViewById(R.id.switchCompatLearned);
        textViewWordPairsPractice = findViewById(R.id.textViewWordPairsPractice);

        recyclerViewWordPairs.setLayoutManager(new GridLayoutManager(this, 2));
    }

    /**
     * Recupera el objeto UserDTO pasado a través del Intent.
     */
    private void retrieveUserFromIntent() {
        this.user = (UserDTO) getIntent().getSerializableExtra("user");
        if (this.user == null) {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**
     * Observa las categorías disponibles para el usuario y actualiza la UI en consecuencia.
     */
    private void observeCategories() {
        categoryViewModel.getAllCategories(this.user.getId()).observe(this, categories -> {
            if (categories != null) {
                showCategorySelection(categories);
            } else {
                Toast.makeText(this, "No se pudieron cargar las categorías", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Configura el AutoCompleteTextView para seleccionar categorías.
     */
    private void setupAutoCompleteCategory() {
        autoCompleteCategory.setThreshold(1); // Mostrar sugerencias desde el primer carácter

        autoCompleteCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No se necesita
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (customListAdapter != null) {
                    customListAdapter.getFilter().filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No se necesita
            }
        });

        autoCompleteCategory.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && customListAdapter != null) {
                customListAdapter.getFilter().filter("");
                autoCompleteCategory.showDropDown();
            }
        });

        autoCompleteCategory.setOnClickListener(v -> {
            if (!autoCompleteCategory.isPopupShowing() && customListAdapter != null) {
                autoCompleteCategory.showDropDown();
            }
        });

        autoCompleteCategory.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = customListAdapter.getItem(position);
            CustomListDTO itemByName = customListAdapter.getItemByName(selectedItem);
            if (itemByName != null) {
                this.currentListId = itemByName.getId();
                loadWordsByListId(currentListId);
            }
        });
    }

    /**
     * Configura el botón para iniciar la práctica con la lista seleccionada.
     */
    private void setupButtonPlayList() {
        buttonPlayList.setOnClickListener(v -> {
            if (currentListId == null) {
                Toast.makeText(this, "Seleccione una lista para practicar", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, FlashcardGameActivity.class);
            intent.putExtra("customListId", currentListId);
            intent.putExtra("user", this.user);
            intent.putExtra("origin", 2);
            startActivity(intent);
        });
    }

    /**
     * Configura el Switch para filtrar palabras aprendidas.
     */
    private void setupSwitchLearned() {
        switchCompatLearned.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (customWordAdapter != null) {
                customWordAdapter.setShowOnlyNotLearned(isChecked);
                customWordAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Muestra las categorías disponibles en el Spinner y configura su comportamiento.
     *
     * @param categories Lista de categorías disponibles.
     */
    private void showCategorySelection(List<CategoryDTO> categories) {
        List<String> categoryNames = categories.stream()
                .map(CategoryDTO::getName)
                .collect(Collectors.toList());

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinnerPractice.setAdapter(categoryAdapter);
        categorySpinnerPractice.setVisibility(View.VISIBLE);

        categorySpinnerPractice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CategoryDTO selectedCategory = categories.get(position);
                loadListsByCategory(selectedCategory.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se seleccionó nada
            }
        });
    }

    /**
     * Carga las listas de palabras asociadas a una categoría específica.
     *
     * @param categoryId ID de la categoría seleccionada.
     */
    private void loadListsByCategory(String categoryId) {
        customListModel.getListsByCategoryId(categoryId).observe(this, customLists -> {
            if (customLists != null) {
                updateCustomListView(customLists);
            } else {
                Toast.makeText(this, "No se pudieron cargar las listas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Actualiza el AutoCompleteTextView con las listas de palabras disponibles.
     *
     * @param customListDTOS Lista de listas de palabras.
     */
    private void updateCustomListView(List<CustomListDTO> customListDTOS) {
        List<String> names = customListDTOS.stream()
                .map(CustomListDTO::getName)
                .collect(Collectors.toList());

        if (customListAdapter == null) {
            customListAdapter = new CustomListPracticeAdapter(this, names, customListDTOS);
            autoCompleteCategory.setAdapter(customListAdapter);
        } else {
            customListAdapter.setCategories(customListDTOS);
            customListAdapter.getFilter().filter(autoCompleteCategory.getText().toString());
        }

        autoCompleteCategory.post(() -> {
            if (!autoCompleteCategory.isPopupShowing()) {
                autoCompleteCategory.showDropDown();
            }
        });
    }

    /**
     * Carga las palabras asociadas a una lista específica y actualiza el RecyclerView.
     *
     * @param listId ID de la lista seleccionada.
     */
    private void loadWordsByListId(String listId) {
        customWordViewModel.getAllWordsWithLearnedMark(this.user.getId(), listId).observe(this, customWords -> {
            if (customWords != null) {
                customWordList.clear();
                customWordList.addAll(customWords);
                if (customWordAdapter == null) {
                    customWordAdapter = new CustomWordAdapterOnPractice(customWordList, this, this);
                    recyclerViewWordPairs.setAdapter(customWordAdapter);
                } else {
                    customWordAdapter.setWordList(customWordList);
                    // O si tienes un método específico para actualizar
                    // customWordAdapter.updateWords(customWordList);
                }
                updateWordsCounter(customWordList.size());
            } else {
                Toast.makeText(this, "No se pudieron cargar las palabras", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Actualiza el contador de palabras mostradas en la interfaz.
     *
     * @param size Número de palabras.
     */
    private void updateWordsCounter(int size) {
        String totalWordsTxt = size == 0 ? "Nada por acá" :
                size == 1 ? "Una palabra" : size + " palabras";
        textViewWordPairsPractice.setText(totalWordsTxt);
    }

    @Override
    public void onUnmarkClick(int userProgressId) {
        // Implementar lógica si es necesario
    }

    @Override
    public void onFilterResults(int count) {
        updateWordsCounter(count);
    }

    @Override
    public void onForgetCustomWord(String wordId) {
        customUserProgressViewModel.deleteLearnedCustomUserProgressByWordId(wordId);
    }

    @Override
    public void onLearnCustomWord(String wordId) {
        customUserProgressViewModel.insertCustomUserProgress(wordId);
    }
}
