package com.faridroid.english10k;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.faridroid.english10k.data.entity.User;
import com.faridroid.english10k.viewmodel.UserViewModel;
import com.faridroid.english10k.viewmodel.dto.UserDTO;

public class FlashcardsSettingsActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    //Get this in base to user level
    private int minWords = 5;
    private long maxWords = 100;
    private UserDTO user;

    Spinner rangeSpinner;
    SeekBar wordCountSeekBar;
    TextView wordCountText;
    Button startFlashcardsButton;
    Button incrementButton;
    Button decrementButton;
    private UserViewModel userViewModel;
    private Button btnGoHome;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_settings);

        wordCountSeekBar = findViewById(R.id.word_count_seekbar);
        wordCountText = findViewById(R.id.word_count_text);
        wordCountSeekBar.setOnSeekBarChangeListener(this);

        startFlashcardsButton = findViewById(R.id.start_flashcards_button);
        wordCountSeekBar.setOnSeekBarChangeListener(this);
        incrementButton = findViewById(R.id.increment_button);
        decrementButton = findViewById(R.id.decrement_button);

        startFlashcardsButton.setOnClickListener(this);
        incrementButton.setOnClickListener(this);
        decrementButton.setOnClickListener(this);

        userViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(UserViewModel.class);

        btnGoHome = findViewById(R.id.btnGoHome);
        btnGoHome.setOnClickListener(this);
        this.user = (UserDTO) getIntent().getSerializableExtra("user");
        setRange();
    }

    private void setRange() {

        this.minWords = 5;
        //todo: change the  10000 to the total amount of words in db
        this.maxWords = Math.min(10000,user.getXp() + 100);

        wordCountSeekBar.setMax((int) maxWords);
        wordCountSeekBar.setMin(minWords);
        wordCountSeekBar.setProgress((int) ((maxWords)/2));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (seekBar.getId() == R.id.word_count_seekbar){
            wordCountText.setText(progress + " palabras");
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    @Override
    public void onClick(View view) {
        int currentValue = wordCountSeekBar.getProgress();


        final Integer incrementButton1 = R.id.increment_button;


        if (view.getId() == R.id.increment_button) {
            int incrementValue = getIncrementValueBasedOnMax(currentValue + minWords);
            if (currentValue + incrementValue <= wordCountSeekBar.getMax()) {
                wordCountSeekBar.setProgress(currentValue + incrementValue);
            } else {
                wordCountSeekBar.setProgress(wordCountSeekBar.getMax());
            }
        } else if (view.getId() == R.id.decrement_button) {
            int decrementValue = getIncrementValueBasedOnMax(currentValue - minWords);
            if (currentValue - decrementValue >= 0) {
                wordCountSeekBar.setProgress(currentValue - decrementValue);
            } else {
                wordCountSeekBar.setProgress(0);
            }
        }else if (view.getId() == R.id.start_flashcards_button){
            Intent intent = new Intent(FlashcardsSettingsActivity.this, FlashcardGameActivity.class);
            intent.putExtra("maxWords", this.maxWords);
            intent.putExtra("user", this.user);
            int wordsToPlay = wordCountSeekBar.getProgress();
            intent.putExtra("wordsToPlay", wordsToPlay);
            startActivity(intent);
        }else if (view.getId() == R.id.btnGoHome){
            Intent intent = new Intent(FlashcardsSettingsActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
    private int getIncrementValueBasedOnMax(int value) {
        if (value <= 100) {
            return 10; // Incremento mínimo de 10 para valores entre 10 y 100
        } else if (value <= 0.1 * maxWords) {
            return (int) (0.01 * maxWords); // Incremento del 1% del máximo
        } else if (value <= 0.5 * maxWords) {
            return (int) (0.05 * maxWords); // Incremento del 5% del máximo
        } else {
            return (int) (0.1 * maxWords); // Incremento del 10% del máximo para valores altos
        }
    }
}