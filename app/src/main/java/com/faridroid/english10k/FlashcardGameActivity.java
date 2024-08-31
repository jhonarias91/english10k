package com.faridroid.english10k;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.faridroid.english10k.data.entity.Word;
import com.faridroid.english10k.game.FlashcardGameManager;
import com.faridroid.english10k.utils.SwipeGestureListener;
import com.faridroid.english10k.viewmodel.UserViewModel;
import com.faridroid.english10k.viewmodel.WordViewModel;
import com.faridroid.english10k.viewmodel.dto.UserDTO;

import java.util.Locale;

public class FlashcardGameActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, View.OnDragListener, SensorEventListener {

    private FlashcardGameManager gameManager;

    private int maxWords;
    private int wordsToPlay;
    private UserDTO user;
    private WordViewModel wordViewModel;
    private View flashcardView;
    private FrameLayout frameCardLayoutContainer;

    private TextView textViewFront, textViewBack;
    private GestureDetector gestureDetector;
    private View viewToAnimateOnSwipe;
    private UserViewModel userViewModel;
    private LinearLayout leftArrowFlashcard, rightArrowFlashcard;

    //Check movement
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float acelVal;  // Valor actual del acelerómetro
    private float acelLast; // Valor previo del acelerómetro
    private float shake;    // Diferencia para detectar un sacudido
    private boolean isOnPause;
    private boolean isShakeDetected = false; // Para evitar múltiples llamadas
    private final long SHAKE_RESET_TIME = 200; // Tiempo en milisegundos para resetear la detección
    private SwipeGestureListener swipeGestureListener;

    private ImageButton imgBtnFlashSpeaker;
    private TextToSpeech textToSpeech;
    private int scoreAdedWord = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_game);

        this.user = (UserDTO) getIntent().getSerializableExtra("user");
        this.maxWords = getIntent().getIntExtra("maxWords", 100);
        this.wordsToPlay = getIntent().getIntExtra("wordsToPlay", 5);
        leftArrowFlashcard = findViewById(R.id.left_arrow_flashcard_id);
        leftArrowFlashcard.setOnClickListener(this);
        rightArrowFlashcard = findViewById(R.id.right_arrow_flashcard_id);
        rightArrowFlashcard.setOnClickListener(this);
        viewToAnimateOnSwipe = findViewById(R.id.flashcardContainer);
        wordViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(WordViewModel.class);

        LinearLayout learnedBox = findViewById(R.id.learnedBox);
        learnedBox.setOnClickListener(this);

        // Inicializar Text-to-Speech
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.US);
            }
        });
        wordViewModel.getWordsByLimit(this.maxWords, this.user.getId()).observe(this, words -> {
            //this.allPosibleWords = words;
            gameManager = new FlashcardGameManager(maxWords, wordsToPlay, words, textToSpeech, this.user, getApplication());
            startGame();
        });

        gestureDetector = new GestureDetector(this, new SwipeGestureListener(this, viewToAnimateOnSwipe));
        //To animate when gyroscope is detected
        swipeGestureListener = new SwipeGestureListener(this, viewToAnimateOnSwipe);

        // Configura el OnTouchListener en la vista raíz
        View rootView = findViewById(R.id.root_flashcard_game_id);

        rootView.setOnTouchListener((view, motionEvent) -> {
            gestureDetector.onTouchEvent(motionEvent);
            return true;
        });

        userViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(UserViewModel.class);

        //Sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;
    }

    private void startGame() {
        isOnPause = false;
        frameCardLayoutContainer = findViewById(R.id.flashcardContainer);
        flashcardView = getLayoutInflater().inflate(R.layout.flashcard_item, frameCardLayoutContainer, false);
        frameCardLayoutContainer.addView(flashcardView);
        Word currentWord = gameManager.getCurrentWord();
        scoreAdedWord = 0;
        if (currentWord == null){
                showEndGameDialog(2);
        }else{
            showFlashcard(currentWord);
        }

    }

    /*
     * Allow to flip a card and move it to the categories.
     * */
    private void showFlashcard(Word word) {

        View cardView = LayoutInflater.from(this).inflate(R.layout.flashcard_item, frameCardLayoutContainer, false);

        textViewFront = cardView.findViewById(R.id.textViewFront);
        textViewBack = cardView.findViewById(R.id.textViewBack);

        textViewFront.setText(word.getWord());  // Mostrar la palabra en inglés
        textViewBack.setText(word.getSpanish()); // Mostrar la traducción en español

        textViewFront.setOnClickListener(this::flipCard);
        textViewBack.setOnClickListener(this::flipCard);

        textViewFront.setOnTouchListener((view, motionEvent) -> {
            gestureDetector.onTouchEvent(motionEvent);
            return false;
        });

        textViewBack.setOnTouchListener((view, motionEvent) -> {
            gestureDetector.onTouchEvent(motionEvent);
            return false;
        });

        imgBtnFlashSpeaker = cardView.findViewById(R.id.imgBtnFlashSpeaker);
        imgBtnFlashSpeaker.setOnClickListener(this);


        // Establecer el estado de visibilidad basado en el historial
        Boolean isFrontVisible = gameManager.isFrontVisible();

        if (isFrontVisible) {
            textViewFront.setVisibility(View.VISIBLE);
            textViewBack.setVisibility(View.GONE);
        } else {
            textViewFront.setVisibility(View.GONE);
            textViewBack.setVisibility(View.VISIBLE);
        }
        frameCardLayoutContainer.addView(cardView);
    }

    /*
     * Check the card to flip it english-spanish or spanish-english
     * */
    private void flipCard(View view) {
        if (isOnPause) {
            return;
        }
        boolean isFrontVisible = gameManager.isFrontVisible();
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
        gameManager.flipCard();
    }

    @Override
    public void onClick(View view) {
        if (isOnPause) {
            return;
        }
        int id = view.getId();


        if (id == R.id.left_arrow_flashcard_id) {
            swipeGestureListener.onSwipeRight();
        } else if (id == R.id.right_arrow_flashcard_id) {
            swipeGestureListener.onSwipeLeft();
        } else if (id == R.id.imgBtnFlashSpeaker) {
            gameManager.speakCurrentWord();
        } else if (id == R.id.learnedBox) {
            gameManager.addLearnedWord();
            userViewModel.updateXp(user.getId(), user.getXp() + 1);
            this.scoreAdedWord++;
            this.user.setXp(user.getXp() + 1);
            if (gameManager.hasNextWord()) {
                if (gameManager.hasMoreWords()){
                    showFlashcard(gameManager.getCurrentWord());
                }else{
                    showEndGameDialog(2);
                }
            } else {
                showEndGameDialog(1);
            }
        }

    }

    @Override
    public boolean onDrag(View v, DragEvent event) {

        return false;

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    //Show left word
    public void onSwipeRightPreviousWord() {
        if (isOnPause) {
            return;
        }
        gameManager.previousWord();
        showFlashcard(gameManager.getCurrentWord());
    }

    //Show right word
    public void onSwipeLeftNextWord() {
        if (isOnPause) {
            return;
        }
        gameManager.nextWord();
        if (gameManager.hasNextWord()) {
            if (gameManager.hasMoreWords()){
                showFlashcard(gameManager.getCurrentWord());
            }else{
                showEndGameDialog(2);
            }
        } else {
            showEndGameDialog(1);
        }
    }

    private void showEndGameDialog(int type) {
        if (isOnPause) {
            return;
        }
        this.isOnPause = true;

        String title = "";
        String body = "";
        switch (type){
            case 1:
                title = "Juego Terminado";
                body = "Tu puntaje es: " + gameManager.getScore();
                break;
            case 2:
                title = "¡Vocabulario completo!";
                body = "Desmarca las aprendidas para volver a jugar.";
                break;
        }
        // create end game dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(body);

        // Botón para volver a jugar
        builder.setPositiveButton("Volver a jugar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (gameManager.getScore() > 0) {
                    userViewModel.updateXp(user.getId(), user.getXp() + gameManager.getScore() + scoreAdedWord);
                    user.setXp(user.getXp() + gameManager.getScore() - scoreAdedWord);
                    scoreAdedWord = 0;
                }

                // Animación de Fade Out
                View rootView = findViewById(android.R.id.content);
                rootView.animate().alpha(0f).setDuration(200).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        // Animación de Fade In
                        rootView.animate().alpha(1f).setDuration(300).start();
                        gameManager.resetGame();
                        isOnPause = false;
                        showFlashcard(gameManager.getCurrentWord());
                    }
                }).start();
            }
        });

        // Botón para salir al menú de configuraciones
        builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (gameManager.getScore() > 0) {
                    userViewModel.updateXp(user.getId(), user.getXp() + gameManager.getScore() - scoreAdedWord);
                }
                // Ir a la actividad de configuraciones
                Intent intent = new Intent(FlashcardGameActivity.this, FlashcardsSettingsActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            }
        });

        // Show dialogo
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isOnPause) {
            return;
        }
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        acelLast = acelVal;
        acelVal = (float) Math.sqrt((double) (x * x + y * y + z * z));
        float delta = acelVal - acelLast;
        //lower values of shake is more sensible
        shake = shake * 0.85f + delta; // Se aplica un filtro para suavizar la señal

        // Check the sensibility
        if (shake > 10 && !isShakeDetected) { // El umbral de detección puede ajustarse
            if (x > 1) {
                swipeGestureListener.onSwipeRight();
            } else if (x < -1) {
                swipeGestureListener.onSwipeLeft();
            }
            isShakeDetected = true;
            // Restablecer el flag después de un tiempo para permitir futuras detecciones
            new Handler().postDelayed(() -> isShakeDetected = false, SHAKE_RESET_TIME);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        isOnPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        isOnPause = false;
    }

    public FlashcardGameManager getGameManager() {
        return gameManager;
    }

}

