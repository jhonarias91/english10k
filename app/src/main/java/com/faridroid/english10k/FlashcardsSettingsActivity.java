package com.faridroid.english10k;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.faridroid.english10k.viewmodel.FlashcardsSettingsViewModel;
import com.faridroid.english10k.viewmodel.WordViewModel;
import com.faridroid.english10k.viewmodel.dto.UserDTO;
import com.faridroid.english10k.viewmodel.factory.FlashcardsSettingsViewModelFactory;

public class FlashcardsSettingsActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    //Get this in base to user level
    private int minWords = 5;
    private long maxWords = 100;
    private UserDTO user;

    Spinner rangeSpinner;
    SeekBar wordCountSeekBar;
    TextView wordCountText;
    Button btnStartGame;
    Button incrementButton;
    Button decrementButton;
    private Button btnGoHome;

    private FlashcardsSettingsViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_settings);

        wordCountSeekBar = findViewById(R.id.word_count_seekbar);
        wordCountText = findViewById(R.id.word_count_text);
        wordCountSeekBar.setOnSeekBarChangeListener(this);

        btnStartGame = findViewById(R.id.start_flashcards_button);
        wordCountSeekBar.setOnSeekBarChangeListener(this);
        incrementButton = findViewById(R.id.increment_button);
        decrementButton = findViewById(R.id.decrement_button);

        btnStartGame.setOnClickListener(this);
        incrementButton.setOnClickListener(this);
        decrementButton.setOnClickListener(this);

        btnGoHome = findViewById(R.id.btnGoHome);
        btnGoHome.setOnClickListener(this);
        this.user = (UserDTO) getIntent().getSerializableExtra("user");

        WordViewModel wordViewModel = wordViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(WordViewModel.class);

        SharedPreferences preferences = getSharedPreferences("english10k_settings", MODE_PRIVATE);


        // Crear la instancia del ViewModelFactory
        FlashcardsSettingsViewModelFactory factory = new FlashcardsSettingsViewModelFactory(getApplication(), wordViewModel, preferences, user);
        // Instanciar el ViewModel usando el Factory
        viewModel = new ViewModelProvider(this, factory).get(FlashcardsSettingsViewModel.class);

        setRange();
    }

    private void setRange() {

        viewModel.getRange().observe(this, seekBarRange -> {
            if (seekBarRange != null) {
                minWords = seekBarRange.getMin();
                maxWords = seekBarRange.getMax();
                wordCountSeekBar.setMax((int) maxWords);
                wordCountSeekBar.setMin(minWords);
                wordCountSeekBar.setProgress(seekBarRange.getProgress());
                wordCountText.setText(seekBarRange.getProgress() + " Palabras");
            }
        });
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

            viewModel.setRange(wordsToPlay);

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