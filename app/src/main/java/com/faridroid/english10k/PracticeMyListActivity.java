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

    private AutoCompleteTextView autoCompleteCustomList;
    private Button buttonPlayList;
    private RecyclerView recyclerViewWordPairs;
    private CustomWordAdapterOnPractice customWordAdapter;
    private List<WordInterface> customWordList = new ArrayList<>();
    private UserDTO user;
    private String currentCategoryId;
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

        // Inicializa los ViewModels
        customListModel = new ViewModelProvider(this).get(CustomListViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        customWordViewModel = new ViewModelProvider(this).get(CustomWordViewModel.class);
        customUserProgressViewModel = new ViewModelProvider(this).get(CustomUserProgressViewModel.class);

        // Enlaza los componentes de la interfaz
        autoCompleteCustomList = findViewById(R.id.autoCompleteCustomListPractice);
        buttonPlayList = findViewById(R.id.buttonPlayListPractice);
        recyclerViewWordPairs = findViewById(R.id.recyclerViewWordPairsPractice);
        categorySpinnerPractice = findViewById(R.id.categorySpinnerPractice);
        recyclerViewWordPairs.setLayoutManager(new GridLayoutManager(this, 2));
        switchCompatLearned = findViewById(R.id.switchCompatLearned);
        textViewWordPairsPractice = findViewById(R.id.textViewWordPairsPractice);

        this.user = (UserDTO) getIntent().getSerializableExtra("user");

        // Observa las categorías
        categoryViewModel.getAllCategories(this.user.getId()).observe(this, categories -> {
            showCategorySelection(categories);
        });

        // Maneja el filtrado para el AutoCompleteTextView de categorías
        autoCompleteCustomList.addTextChangedListener(new TextWatcher() {
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

        // Botón para comenzar a jugar con la lista seleccionada
        buttonPlayList.setOnClickListener(v -> {
            if (currentListId == null) {
                Toast.makeText(this, "Seleccione una lista para jugar", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, FlashcardGameActivity.class);
            intent.putExtra("listId", currentListId);
            startActivity(intent);
        });

        autoCompleteCustomList.setOnClickListener(v -> {
            if (!autoCompleteCustomList.isPopupShowing() && customListAdapter != null) {
                autoCompleteCustomList.showDropDown();
            }
        });
        autoCompleteCustomList.setThreshold(0); //todo: check this or change to 1
        setBtnPlayGame();

        // Configura el Switch para filtrar
        switchCompatLearned.setOnCheckedChangeListener((buttonView, isChecked) -> {
            customWordAdapter.setShowOnlyNotLearned(isChecked);
        });
    }

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

    private void loadListsByCategory(String categoryId) {
        customListModel.getListsByCategoryId(categoryId).observe(this, customLists -> {
            updateCustomListView(customLists);
        });
    }

    private void updateCustomListView(List<CustomListDTO> customListDTOS) {

        List<String> names = customListDTOS.stream().map(CustomListDTO::getName).collect(Collectors.toList());

        if (customListAdapter == null) {
            customListAdapter = new CustomListPracticeAdapter(this, names, customListDTOS);
            autoCompleteCustomList.setAdapter(customListAdapter);
        } else {
            customListAdapter.setCategories(customListDTOS);
        }

        autoCompleteCustomList.post(() -> {
            if (!autoCompleteCustomList.isPopupShowing()) {
                autoCompleteCustomList.showDropDown();
            }
        });

        autoCompleteCustomList.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = customListAdapter.getItem(position);
            CustomListDTO itemByName = customListAdapter.getItemByName(selectedItem);
            if (itemByName != null) {
                this.currentListId = itemByName.getId();
                customWordViewModel.getAllWordsWithLearnedMark(this.user.getId(), currentListId).observe(this, customWords -> {
                    if (customWords != null) {
                        customWordList.clear();  // Limpia la lista existente
                        customWordList.addAll(customWords);  // Añade las nuevas palabras
                        if (customWordAdapter == null) {
                            customWordAdapter = new CustomWordAdapterOnPractice(customWordList, this, this);

                            recyclerViewWordPairs.setAdapter(customWordAdapter);
                        } else {
                            customWordAdapter.notifyDataSetChanged();  // Notifica los cambios al adaptador
                        }
                    }
                });
            }
        });
    }

    private void setBtnPlayGame() {
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

    @Override
    public void onUnmarkClick(int userProgressId) {
        //nothing
    }

    @Override
    public void onFilterResults(int count) {
        updateWordsCounter(count);
    }

    private void updateWordsCounter(int size) {
        String totalWordsTxt = size == 0 ? "Nada por acá" : size == 1 ? "Una palabra ": String.valueOf(size)+ " palabras";
        textViewWordPairsPractice.setText(totalWordsTxt);
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
