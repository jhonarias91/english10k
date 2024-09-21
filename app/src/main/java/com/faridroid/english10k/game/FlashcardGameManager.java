package com.faridroid.english10k.game;

import android.app.Application;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.faridroid.english10k.data.dto.ProgressType;
import com.faridroid.english10k.data.dto.UserDTO;
import com.faridroid.english10k.data.dto.UserProgressDTO;
import com.faridroid.english10k.data.dto.interfaces.WordInterface;
import com.faridroid.english10k.data.entity.UserCustomProgress;
import com.faridroid.english10k.data.enums.OriginEnum;
import com.faridroid.english10k.service.UserCustomProgressService;
import com.faridroid.english10k.service.UserProgressService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class FlashcardGameManager {

    private int maxWords;
    private int wordsToPlay;
    private int currentCardPosition = 0;
    private int score = 0;
    private Set<Integer> flippedCardPositions = new HashSet<>();
    private List<WordInterface> allPossibleWords;
    //To handle the swipe over the text
    private HashMap<Integer, Boolean> cardVisibilityStates = new HashMap<>();
    private TextToSpeech textToSpeech;
    private UserProgressService userProgressService;
    private UserCustomProgressService userCustomProgressService;
    private UserDTO user;
    private OriginEnum origin;

    public FlashcardGameManager(int maxWords, int wordsToPlay, List<WordInterface> allPossibleWords,
                                TextToSpeech textToSpeech, UserDTO user, OriginEnum origin, Application application) {
        Log.i("FlashcardGameManager", "FlashcardGameManager initialized. currentCardPosition: " + currentCardPosition);
        this.maxWords = maxWords;
        this.wordsToPlay = wordsToPlay;
        this.allPossibleWords = allPossibleWords;
        Collections.shuffle(this.allPossibleWords);
        // Inicializar Text-to-Speech
        this.textToSpeech = textToSpeech;
        this.user = user;
        this.origin = origin;
        userProgressService = UserProgressService.getInstance(application);
        userCustomProgressService = UserCustomProgressService.getInstance(application);
        this.currentCardPosition = 0;
    }

    public WordInterface getCurrentWord() {
        if (currentCardPosition < 0 || currentCardPosition >= allPossibleWords.size()) {
            return null;
        }
        return allPossibleWords.get(currentCardPosition);
    }

    public boolean hasNextWord() {
        return currentCardPosition < wordsToPlay && currentCardPosition >= 0;
    }

    public boolean hasMoreWords() {
        return allPossibleWords != null && !allPossibleWords.isEmpty();

    }

    public void nextWord() {
        if (hasNextWord()) {
            currentCardPosition++;
            Log.i("FlashcardGameManager", "Next word. Current position: " + currentCardPosition);
        } else {
            Log.i("FlashcardGameManager", "No next word available. Current position: " + currentCardPosition);
        }
    }

    public void previousWord() {
        if (currentCardPosition > 0) {
            currentCardPosition--;
        }
    }

    public void flipCard() {
        if (!flippedCardPositions.contains(currentCardPosition)) {
            flippedCardPositions.add(currentCardPosition);
            setScore(score + 1);
        }
        boolean isFrontVisible = cardVisibilityStates.getOrDefault(currentCardPosition, true);
        cardVisibilityStates.put(currentCardPosition, !isFrontVisible);
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isFrontVisible() {
        return cardVisibilityStates.getOrDefault(currentCardPosition, true);
    }

    public int getScore() {
        return score;
    }

    public void resetGame() {
        currentCardPosition = 0;
        score = 0;
        flippedCardPositions.clear();
        cardVisibilityStates.clear();
        Collections.shuffle(allPossibleWords);
    }

    //This play the current word
    public void speakCurrentWord() {
        if (textToSpeech != null) {
            WordInterface currentWord = getCurrentWord();
            if (currentWord != null && currentWord.getWord() != null) {
                textToSpeech.speak(currentWord.getWord(), TextToSpeech.QUEUE_FLUSH, null, null);
            }
        }
    }

    public void addLearnedWord(OriginEnum originEnum) {
        WordInterface currentWord = getCurrentWord();
        if (currentWord == null) {
            Log.w("FlashcardGameManager", "No current word to add as learned.");
            return;
        }
        registerUserProgress(originEnum, currentWord);
        // Asegurarse de que la lista es mutable
        if (allPossibleWords instanceof ArrayList) {
            ((ArrayList<WordInterface>) allPossibleWords).remove(currentWord);
        } else {
            // Si no es mutable, crear una nueva lista mutable
            allPossibleWords = new ArrayList<>(allPossibleWords);
            allPossibleWords.remove(currentWord);
        }
        // Ajustar la posición actual si es necesario
        if (currentCardPosition >= allPossibleWords.size()) {
            currentCardPosition = allPossibleWords.size() - 1;
        }
    }

    private void registerUserProgress(OriginEnum originEnum, WordInterface currentWord) {
        long lastUpdated = System.currentTimeMillis();
        if (OriginEnum.CUSTOM_WORDS.equals(originEnum)) {
            UserCustomProgress userProgressDTO = new UserCustomProgress(UUID.randomUUID().toString(), currentWord.getId(),
                    0, lastUpdated, ProgressType.WORD_LEARNED);
            userCustomProgressService.insertUserProgress(userProgressDTO);
            Log.i("FlashcardGameManager", "Custom word progress inserted.");
        } else if (OriginEnum.ENGLISH10K.equals(originEnum)) {
            Log.i("FlashcardGameManager", "English10K word progress inserted.");
            UserProgressDTO userProgressDTO = new UserProgressDTO(0, Integer.valueOf(currentWord.getId()),
                    user.getId(), 0, 0, ProgressType.WORD_LEARNED);
            userProgressService.insertUserProgress(userProgressDTO);
        }
    }

    public UserDTO getUser() {
        return user;
    }

    public int getCurrentCardPosition() {
        return currentCardPosition;
    }

    public void setCurrentCardPosition(int currentCardPosition) {
        this.currentCardPosition = currentCardPosition;
    }

    public void setFrontVisible(boolean isFrontVisible) {
        cardVisibilityStates.put(currentCardPosition, isFrontVisible);
    }

    public OriginEnum getOrigin() {
        return origin;
    }
}
