package com.faridroid.english10k;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.faridroid.english10k.view.viewmodel.CategoryViewModel;
import com.faridroid.english10k.view.viewmodel.UserViewModel;
import com.faridroid.english10k.view.viewmodel.WordViewModel;
import com.faridroid.english10k.data.dto.UserDTO;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private WordViewModel wordViewModel;
    private UserViewModel userViewModel;
    private TextView txtXp;
    private UserDTO user;
    private CategoryViewModel categoryViewModel;
    private Button btnGoToPracticeList;
    private Button btnCreateMassiveList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(WordViewModel.class);

        txtXp = findViewById(R.id.txtXp);

        Button btnFlashCards = findViewById(R.id.btnFlashCards);
        btnFlashCards.setOnClickListener(this);
        btnGoToPracticeList = findViewById(R.id.btnGoToPracticeList);
        btnGoToPracticeList.setOnClickListener(this);
        Button btnCreateCustomWords = findViewById(R.id.btnCreateCustomWords);
        btnCreateCustomWords.setOnClickListener(this);
        btnCreateMassiveList = findViewById(R.id.btnCreateMassiveList);
        btnCreateMassiveList.setOnClickListener(this);

        userViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(UserViewModel.class);

        categoryViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(CategoryViewModel.class);

        userViewModel.checkOrCreateUser();

        userViewModel.getIsUserCreated().observe(this, isCreated -> {
            if (isCreated) {
                userViewModel.getUserDTO().observe(this, user -> {
                    if (user != null) {
                        this.user = user;
                        categoryViewModel.setUserId(user.getId());
                        displayUserXP();
                        categoryViewModel.getOrCreateDefaultCategory();
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnFlashCards) {
            // Crear un intent para abrir la actividad de Flashcards
            Intent intent = new Intent(MainActivity.this, FlashcardsSettingsActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }else if (view.getId() == R.id.btnCreateCustomWords){
            goDefaultCustomCategory();
        }else if (view.getId() == R.id.btnGoToPracticeList){
            goToPracticeMyList();
        } else if (view.getId() == R.id.btnCreateMassiveList) {
           goToCreateMassiveList();
        }
    }

    private void goToCreateMassiveList() {
        Intent intent = new Intent(MainActivity.this, ImportCustomListActivity.class);
        startActivity(intent);
    }

    private void goDefaultCustomCategory() {
        //Get the category
        categoryViewModel.getOrCreateDefaultCategory().observe(this, categoryDTOs -> {
            if (categoryDTOs != null) {
                Intent intent = new Intent(MainActivity.this, AddWordToCustomListActivity.class);
                intent.putExtra("userId", user.getId());
                intent.putExtra("categoryId", categoryDTOs.getId());
                startActivity(intent);
            }
        });
    }

    private void goToPracticeMyList() {
        Intent intent = new Intent(MainActivity.this, PracticeMyListActivity.class);
        intent.putExtra("user", this.user);
        startActivity(intent);
    }

    private void displayUserXP() {
        txtXp.setText(String.format("Puntaje: %d", this.user.getXp()));
    }
}
