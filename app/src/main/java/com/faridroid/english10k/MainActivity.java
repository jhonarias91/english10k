package com.faridroid.english10k;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.faridroid.english10k.data.entity.User;
import com.faridroid.english10k.data.entity.Word;

import java.util.List;

import com.faridroid.english10k.viewmodel.UserViewModel;
import com.faridroid.english10k.viewmodel.WordViewModel;
import com.faridroid.english10k.viewmodel.dto.UserDTO;

import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private WordViewModel wordViewModel;
    private UserViewModel userViewModel;
    private TextView txtXp;
    private UserDTO user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(WordViewModel.class);

        txtXp = findViewById(R.id.txtXp);

        Button btnFlashCards = findViewById(R.id.btnFlashCards);
        btnFlashCards.setOnClickListener(this);

        userViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(UserViewModel.class);

        userViewModel.checkOrCreateUser();

        userViewModel.getIsUserCreated().observe(this, isCreated -> {
            if (isCreated) {
                userViewModel.getUserDTO().observe(this, user -> {
                    if (user != null) {
                        this.user = user;
                        displayUserXP();
                    }
                });
            } else {
                //Issue with user
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
        }
    }

    private void displayUserXP() {
        txtXp.setText(String.format("Puntaje: %d", this.user.getXp()));
    }
}
