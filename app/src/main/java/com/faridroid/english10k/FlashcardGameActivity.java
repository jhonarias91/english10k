package com.faridroid.english10k;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.faridroid.english10k.data.entity.Word;
import com.faridroid.english10k.utils.SwipeGestureListener;
import com.faridroid.english10k.viewmodel.UserViewModel;
import com.faridroid.english10k.viewmodel.WordViewModel;
import com.faridroid.english10k.viewmodel.dto.UserDTO;

import java.util.Collections;
import java.util.List;

public class FlashcardGameActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, View.OnDragListener {


    private int maxWords;
    private int wordsToPlay;
    private UserDTO user;
    private WordViewModel wordViewModel;
    private List<Word> allPosibleWords;
    private List<Word> levelWords;
    private View flashcardView;
    private FrameLayout frameCardLayoutContainer;
    private int currentCardPosition = 0;
    private int score = 0;
    private TextView textViewFront, textViewBack;
    private boolean isFrontVisible = true;

    private GestureDetector gestureDetector;
    private View viewToAnimateOnSwipe;
    private UserViewModel userViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_game);

        this.user = (UserDTO) getIntent().getSerializableExtra("user");
        this.maxWords = getIntent().getIntExtra("maxWords", 100);
        this.wordsToPlay = getIntent().getIntExtra("wordsToPlay", 5);

        viewToAnimateOnSwipe = findViewById(R.id.flashcardContainer);
        wordViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(WordViewModel.class);

        wordViewModel.getWordsByLimit(this.maxWords).observe(this, words -> {
            this.allPosibleWords = words;
            startGame();
        });

        gestureDetector = new GestureDetector(this, new SwipeGestureListener(this, viewToAnimateOnSwipe));
        // Configura el OnTouchListener en la vista raíz
        View rootView = findViewById(R.id.root_flashcard_game_id);
        rootView.setOnTouchListener((view, motionEvent) -> {
            gestureDetector.onTouchEvent(motionEvent);
            return true;
        });


        userViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(UserViewModel.class);

    }

    private void startGame() {
        //Logic for flashCard
        frameCardLayoutContainer = findViewById(R.id.flashcardContainer);
        flashcardView = getLayoutInflater().inflate(R.layout.flashcard_item, frameCardLayoutContainer, false);
        frameCardLayoutContainer.addView(flashcardView);
        Collections.shuffle(allPosibleWords);
        showFlashcard(allPosibleWords.get(currentCardPosition));
    }

    private void showFlashcard(Word word) {
        frameCardLayoutContainer = findViewById(R.id.flashcardContainer);
        frameCardLayoutContainer.removeAllViews();
        View cardView = LayoutInflater.from(this).inflate(R.layout.flashcard_item, frameCardLayoutContainer, false);

        textViewFront = cardView.findViewById(R.id.textViewFront);
        textViewBack = cardView.findViewById(R.id.textViewBack);

        textViewFront.setText(word.getWord());  // Mostrar la palabra en inglés
        textViewBack.setText(word.getSpanish()); // Mostrar la traducción en español

        textViewFront.setOnClickListener(this::flipCard);
        textViewBack.setOnClickListener(this::flipCard);

        frameCardLayoutContainer.addView(cardView);
    }

    private void flipCard(View view) {
        if (isFrontVisible) {
            textViewFront.animate().rotationY(90).setDuration(200).withEndAction(() -> {
                textViewFront.setVisibility(View.GONE);
                textViewBack.setVisibility(View.VISIBLE);
                textViewBack.setRotationY(-90);
                textViewBack.animate().rotationY(0).setDuration(200);
            });
        } else {
            textViewBack.animate().rotationY(90).setDuration(200).withEndAction(() -> {
                textViewBack.setVisibility(View.GONE);
                textViewFront.setVisibility(View.VISIBLE);
                textViewFront.setRotationY(-90);
                textViewFront.animate().rotationY(0).setDuration(200);
            });
        }
        isFrontVisible = !isFrontVisible;
    }

    @Override
    public void onClick(View view) {

     /*   if (view.getId() == R.id.txtFlashcardWord){
            // Mostrar la traducción de la palabra
            TextView txtFlashcardWord = findViewById(R.id.txtFlashcardWord);
            txtFlashcardWord.setText(allPosibleWords.get(currentCardPosition).getSpanish());
        }*/
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        return false;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }


    //Show left word
    public void onSwipeRight() {
        if (currentCardPosition > 0) {
            currentCardPosition--;
            showFlashcard(allPosibleWords.get(currentCardPosition));
            score--;
        }
    }

    //Show right word
    public void onSwipeLeft() {
        if (currentCardPosition < wordsToPlay - 1) {
            currentCardPosition++;
            showFlashcard(allPosibleWords.get(currentCardPosition));
            score++;
        } else if (currentCardPosition == wordsToPlay - 1) {
            score++;
            showEndGameDialog();
        }
    }

    private void showEndGameDialog() {

        this.user.setXp(this.user.getXp() + this.score);

        // Crear el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Juego Terminado");
        builder.setMessage("Tu puntaje es: " + this.score + "\n¿Qué te gustaría hacer ahora?");

        // Botón para volver a jugar
        builder.setPositiveButton("Volver a jugar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetGame();
            }
        });

        // Botón para salir al menú de configuraciones
        builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                userViewModel.updateXp(user.getId(), user.getXp());
                // Ir a la actividad de configuraciones
                Intent intent = new Intent(FlashcardGameActivity.this, FlashcardsSettingsActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });

        // Mostrar el dialogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void resetGame() {
        currentCardPosition = 0;
        score = 0;
        startGame();
    }
}

