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
import android.os.Looper;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.faridroid.english10k.data.dto.UserDTO;
import com.faridroid.english10k.data.dto.interfaces.WordInterface;
import com.faridroid.english10k.data.enums.OriginEnum;
import com.faridroid.english10k.game.FlashcardGameManager;
import com.faridroid.english10k.utils.SwipeGestureListener;
import com.faridroid.english10k.view.viewmodel.CustomWordViewModel;
import com.faridroid.english10k.view.viewmodel.UserViewModel;
import com.faridroid.english10k.view.viewmodel.WordViewModel;

import java.util.Locale;

public class FlashcardGameActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

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
    private static final float SHAKE_THRESHOLD = 12.0f; // Umbral de sensibilidad

    private SwipeGestureListener swipeGestureListener;
    private OriginEnum originEnum;


    private ImageButton imgBtnFlashSpeaker;
    private TextToSpeech textToSpeech;
    private int scoreLearnedWord = 0;

    private CustomWordViewModel   customWordViewModel;
    private String customListId;

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_game);

        this.user = (UserDTO) getIntent().getSerializableExtra("user");
        //get originEnum from intent
        int originInt = getIntent().getIntExtra("origin",1);
        this.originEnum = OriginEnum.getByValue(originInt);

        leftArrowFlashcard = findViewById(R.id.left_arrow_flashcard_id);
        leftArrowFlashcard.setOnClickListener(this);
        rightArrowFlashcard = findViewById(R.id.right_arrow_flashcard_id);
        rightArrowFlashcard.setOnClickListener(this);
        viewToAnimateOnSwipe = findViewById(R.id.flashcardContainer);


        userViewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(UserViewModel.class);


        LinearLayout learnedBox = findViewById(R.id.learnedBox);
        learnedBox.setOnClickListener(this);

        // Initialize Text-to-Speech
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(Locale.US);
            }
        });

        if (OriginEnum.ENGLISH10K.equals(originEnum)) {

            int type = getIntent().getExtras().getInt("type", 1);
            this.maxWords = getIntent().getIntExtra("maxWords", 100);
            this.wordsToPlay = getIntent().getIntExtra("wordsToPlay", 5);

            //todo: check to send maxWords
            wordViewModel = new ViewModelProvider(this,
                    ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(WordViewModel.class);
            wordViewModel.getWordsByLimit(this.wordsToPlay, this.user.getId(), type).observe(this, words -> {
                //this.allPosibleWords = words;
                if (gameManager == null) {
                    gameManager = new FlashcardGameManager(maxWords, wordsToPlay, words, textToSpeech, this.user, originEnum, getApplication());
                }
                startGame();
            });

        } else if (OriginEnum.CUSTOM_WORDS.equals(originEnum)) {
            customWordViewModel = new ViewModelProvider(this,
                    ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(CustomWordViewModel.class);

            customListId = getIntent().getStringExtra("customListId");

            customWordViewModel.getNonLearnedCustomWordsByList(this.user.getId(), customListId).observe(this, words -> {
                if (gameManager == null) {
                    gameManager = new FlashcardGameManager(words.size(), words.size(), words, textToSpeech, this.user,originEnum, getApplication());
                }
                if (words == null || words.isEmpty()) {
                    showEndGameDialog(2);
                    return;
                }
                startGame();
            });
        }

        //todo change to interface
        gestureDetector = new GestureDetector(this, new SwipeGestureListener(this, viewToAnimateOnSwipe));
        //To animate when gyroscope is detected
        swipeGestureListener = new SwipeGestureListener(this, viewToAnimateOnSwipe);

        // Configura el OnTouchListener en la vista raíz
        View rootView = findViewById(R.id.root_flashcard_game_id);

        rootView.setOnTouchListener((view, motionEvent) -> {
            gestureDetector.onTouchEvent(motionEvent);
            return true;
        });


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
        WordInterface currentWord = gameManager.getCurrentWord();

        if (currentWord == null){
                showEndGameDialog(2);
        }else{
            showFlashcard(currentWord);
        }
    }

    /*
     * Allow to flip a card and move it to the categories.
     * */
    private void showFlashcard(WordInterface word) {
        Log.i("FlashcardGameActivity", "Showing word at position: " + gameManager.getCurrentCardPosition());
        // Remove existing flashcard views
        frameCardLayoutContainer.removeAllViews();

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
        if (isOnPause || gameManager == null) {
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
            //Add learned word
            gameManager.addLearnedWord(this.originEnum);
            this.scoreLearnedWord++;

            // Comprueba si hay más palabras para jugar
            if (gameManager.hasNextWord()) {
                showFlashcard(gameManager.getCurrentWord()); // Muestra la siguiente palabra
            } else if (!gameManager.hasMoreWords()) {
                showEndGameDialog(2); // Muestra el diálogo si no quedan más palabras en la lista total
            } else {
                showEndGameDialog(1); // Muestra el diálogo si se completó el set de palabras
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    //Show left word
    public void onSwipeRightPreviousWord() {
        if (isOnPause) {
            return;
        }
        gameManager.previousWord();
        showFlashcard(gameManager.getCurrentWord());
    }


    public void onSwipeLeftNextWord() {
        if (isOnPause) {
            return;
        }
        // Verificar si hay una palabra siguiente antes de avanzar
        if (gameManager.hasNextWord()) {
            gameManager.nextWord();
            WordInterface currentWord = gameManager.getCurrentWord();
            if (currentWord != null) {
                showFlashcard(currentWord);
            } else {
                showEndGameDialog(1);
            }
        } else {
            // No hay más palabras disponibles
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
        int total = gameManager.getScore() + this.scoreLearnedWord;
        switch (type){
            case 1:
                title = "Juego Terminado";
                body = "Tu puntaje es: " + total;
                break;
            case 2:
                title = "¡Conjunto completo! puntaje: "+ total;
                body = "Vuelve a seleccioón de palabras para volver a jugar";
                break;
        }
        // create end game dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(body);

        // Just in case more words availables
        if (type != 2){
            builder.setPositiveButton("Volver a jugar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (gameManager.getScore() > 0 || scoreLearnedWord > 0) {
                        int totalXp = gameManager.getScore() + scoreLearnedWord;
                        userViewModel.updateXp(user.getId(), user.getXp() + totalXp);
                        user.setXp(user.getXp() + totalXp);
                        scoreLearnedWord = 0;
                    }

                    //Fade Out
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
        }

        // Botón para salir al menú de configuraciones
        builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (gameManager.getScore() > 0) {
                    userViewModel.updateXp(user.getId(), user.getXp() + gameManager.getScore() - scoreLearnedWord);
                }
                if (gameManager.getOrigin() == OriginEnum.ENGLISH10K){
                    Intent intent = new Intent(FlashcardGameActivity.this, FlashcardsSettingsActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                }else if (gameManager.getOrigin() == OriginEnum.CUSTOM_WORDS){
                    Intent intent = new Intent(FlashcardGameActivity.this, PracticeMyListActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    finish();
                }

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
        shake = shake * 0.9f + delta; // Se aplica un filtro para suavizar la señal

        // Check the sensibility
        if (shake > SHAKE_THRESHOLD  && !isShakeDetected) { // El umbral de detección puede ajustarse
            if (x > 1) {
                swipeGestureListener.onSwipeRight();
            } else if (x < -1) {
                swipeGestureListener.onSwipeLeft();
            }
            isShakeDetected = true;
            // Restablecer el flag después de un tiempo para permitir futuras detecciones
            handler.postDelayed(() -> isShakeDetected = false, SHAKE_RESET_TIME);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        handler.removeCallbacksAndMessages(null);
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
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentCardPosition", gameManager.getCurrentCardPosition());
        outState.putInt("scoreLearnedWord", scoreLearnedWord);
        outState.putInt("gameScore", gameManager.getScore());
        outState.putBoolean("isFrontVisible", gameManager.isFrontVisible());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            gameManager.setCurrentCardPosition(savedInstanceState.getInt("currentCardPosition"));
            scoreLearnedWord = savedInstanceState.getInt("scoreLearnedWord");
            gameManager.setScore(savedInstanceState.getInt("gameScore"));
            gameManager.setFrontVisible(savedInstanceState.getBoolean("isFrontVisible"));
            showFlashcard(gameManager.getCurrentWord());
        }
    }


}

