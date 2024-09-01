package com.faridroid.english10k;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faridroid.english10k.service.UserProgressService;
import com.faridroid.english10k.view.adapter.LearnedWordsAdapter;
import com.faridroid.english10k.view.adapter.OnLearnedWordClickListener;
import com.faridroid.english10k.viewmodel.dto.ProgressType;
import com.faridroid.english10k.viewmodel.dto.UserDTO;
import com.faridroid.english10k.viewmodel.dto.UserProgressWordJoinDTO;

import java.util.List;

public class LearnedWordsActivity extends AppCompatActivity implements OnLearnedWordClickListener {

    private RecyclerView recyclerViewLearnedWords;
    private LearnedWordsAdapter learnedWordsAdapter;
    private UserProgressService userProgressService;
    private UserDTO user;
    private TextView textViewLearnedWordsCounter;
    private EditText editTextSearchLearnedWords;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learned_words);

        //Read the extras to get the user id
        Bundle extras = getIntent().getExtras();
        this.user = (UserDTO) getIntent().getSerializableExtra("user");

        ProgressType type = extras != null && extras.containsKey("progressType")
                ? (ProgressType) extras.get("progressType")
                : ProgressType.WORD_LEARNED;

        recyclerViewLearnedWords = findViewById(R.id.recyclerViewLearnedWords);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);//2 columns
        recyclerViewLearnedWords.setLayoutManager(gridLayoutManager);

        userProgressService = UserProgressService.getInstance(getApplication());
        textViewLearnedWordsCounter = findViewById(R.id.textViewLearnedWordsCounter);
        editTextSearchLearnedWords = findViewById(R.id.editTextSearchLearnedWords);


        // Configurar la Toolbar como ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Observa los datos
        userProgressService.listUserProgressWithWord(this.user.getId(), type)
                .observe(this, new Observer<List<UserProgressWordJoinDTO>>() {
                    @Override
                    public void onChanged(List<UserProgressWordJoinDTO> userProgressWordJoinDTOS) {
                        // Configura el adaptador cuando los datos cambian
                        learnedWordsAdapter = new LearnedWordsAdapter(userProgressWordJoinDTOS, LearnedWordsActivity.this);
                        recyclerViewLearnedWords.setAdapter(learnedWordsAdapter);
                        int size = userProgressWordJoinDTOS.size();
                        String totalWordsTxt = size == 0 ? "Nada por acá" : size == 1 ? "Una palabra ": String.valueOf(size)+ " palabras";
                        textViewLearnedWordsCounter.setText(totalWordsTxt);
                    }
                });

        // Agrega un TextWatcher para filtrar la lista
        editTextSearchLearnedWords.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterWords(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });


    }

    private void filterWords(String query) {
        if (learnedWordsAdapter != null) {
            learnedWordsAdapter.filter(query);
        }
    }

    private void updateWordsCounter(int size) {
        String totalWordsTxt = size == 0 ? "Nada por acá" : size == 1 ? "Una palabra ": String.valueOf(size)+ " palabras";
        textViewLearnedWordsCounter.setText(totalWordsTxt);
    }

    @Override
    public void onUnmarkClick(int userProgressId) {
        userProgressService.deleteUserProgress(userProgressId);
    }

    @Override
    public void onFilterResults(int count) {
        updateWordsCounter(count);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
