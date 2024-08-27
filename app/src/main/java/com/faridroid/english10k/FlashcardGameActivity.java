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
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FlashcardGameActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, View.OnDragListener, SensorEventListener {


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
    //To save the position of the flipped cards
    private Set<Integer> flippedCardPositions = new HashSet<>();

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
    private boolean isOnPause ;
    private boolean isShakeDetected = false; // Para evitar múltiples llamadas
    private final long SHAKE_RESET_TIME = 500; // Tiempo en milisegundos para resetear la detección
    private SwipeGestureListener swipeGestureListener;

    //To solve issue with the swipe over the text
    private HashMap<Integer, Boolean> cardVisibilityStates;

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

        wordViewModel.getWordsByLimit(this.maxWords).observe(this, words -> {
            this.allPosibleWords = words;
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
        //Logic for flashCard
        isOnPause = false;
        frameCardLayoutContainer = findViewById(R.id.flashcardContainer);
        flashcardView = getLayoutInflater().inflate(R.layout.flashcard_item, frameCardLayoutContainer, false);
        frameCardLayoutContainer.addView(flashcardView);
        Collections.shuffle(allPosibleWords);
        cardVisibilityStates = new HashMap<>();

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
        textViewFront.setOnTouchListener((view, motionEvent) -> {
            gestureDetector.onTouchEvent(motionEvent);
            //Return false to allow the event to continue to any other listeners
            return false;
        });

        textViewBack.setOnTouchListener((view, motionEvent) -> {
            gestureDetector.onTouchEvent(motionEvent);
            //Return false to allow the event to continue to any other listeners
            return false;
        });

        // Establecer el estado de visibilidad basado en el historial
        Boolean isFrontVisible = cardVisibilityStates.getOrDefault(currentCardPosition, true);
        if (!isFrontVisible) {
            textViewFront.setVisibility(View.GONE);
            textViewBack.setVisibility(View.VISIBLE);
        } else {
            textViewFront.setVisibility(View.VISIBLE);
            textViewBack.setVisibility(View.GONE);
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
        boolean isFrontVisible = cardVisibilityStates.getOrDefault(currentCardPosition, true);

        if (isFrontVisible) {
            textViewFront.animate().rotationY(90).setDuration(200).withEndAction(() -> {
                textViewFront.setVisibility(View.GONE);
                textViewBack.setVisibility(View.VISIBLE);
                textViewBack.setRotationY(-90);
                textViewBack.animate().rotationY(0).setDuration(200);
            });
            // Verifica si la palabra en la posición actual ya ha sido volteada
            if (!flippedCardPositions.contains(currentCardPosition)) {
                flippedCardPositions.add(currentCardPosition); // Marca la posición como volteada
                score++; // Incrementa el puntaje
            }
            // Actualizar el mapa
            cardVisibilityStates.put(currentCardPosition, false);
        } else {
            textViewBack.animate().rotationY(90).setDuration(200).withEndAction(() -> {
                textViewBack.setVisibility(View.GONE);
                textViewFront.setVisibility(View.VISIBLE);
                textViewFront.setRotationY(-90);
                textViewFront.animate().rotationY(0).setDuration(200);
            });
            // Actualizar el mapa
            cardVisibilityStates.put(currentCardPosition, true);
        }
    }

    @Override
    public void onClick(View view) {

        if (isOnPause) {
            return;
        }
        if (view.getId() == R.id.left_arrow_flashcard_id) {
            onSwipeRight();
        } else if (view.getId() == R.id.right_arrow_flashcard_id) {
            onSwipeLeft();
        }
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
        if (isOnPause) {
            return;
        }
        if (currentCardPosition > 0) {
            currentCardPosition--;
            showFlashcard(allPosibleWords.get(currentCardPosition));
        }
    }

    //Show right word
    public void onSwipeLeft() {
        if (isOnPause) {
            return;
        }
        if (currentCardPosition < wordsToPlay - 1) {
            currentCardPosition++;
            showFlashcard(allPosibleWords.get(currentCardPosition));
        } else if (currentCardPosition == wordsToPlay - 1) {
            showEndGameDialog();
        }
    }

    private void showEndGameDialog() {
        if (isOnPause) {
            return;
        }
        this.isOnPause = true;

        // Crear el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Juego Terminado");
        builder.setMessage("Tu puntaje es: " + this.score );

        // Botón para volver a jugar
        builder.setPositiveButton("Volver a jugar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (score > 0) {
                    userViewModel.updateXp(user.getId(), user.getXp() + score);
                    user.setXp(user.getXp() + score);
                }
                resetGame();
            }
        });

        // Botón para salir al menú de configuraciones
        builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (score > 0) {
                    userViewModel.updateXp(user.getId(), user.getXp() + score);
                }

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
        isOnPause = false;
        flippedCardPositions.clear();
        cardVisibilityStates.clear();

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
        shake = shake * 0.9f + delta; // Se aplica un filtro para suavizar la señal


        // Aquí se comprueba la dirección del movimiento
        if (shake > 12 && !isShakeDetected) { // El umbral de detección puede ajustarse
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
        // No se necesita implementar nada aquí para este caso
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
}

