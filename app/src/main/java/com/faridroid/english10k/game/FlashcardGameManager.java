package com.faridroid.english10k.game;

import android.app.Application;
import android.speech.tts.TextToSpeech;

import com.faridroid.english10k.data.entity.Word;
import com.faridroid.english10k.service.UserProgressService;
import com.faridroid.english10k.data.dto.ProgressType;
import com.faridroid.english10k.data.dto.UserDTO;
import com.faridroid.english10k.data.dto.UserProgressDTO;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FlashcardGameManager {

    private int maxWords;
    private int wordsToPlay;
    private int currentCardPosition = 0;
    private int score = 0;
    private Set<Integer> flippedCardPositions = new HashSet<>();
    private List<Word> allPosibleWords;
    //To handle the swipe over the text
    private HashMap<Integer, Boolean> cardVisibilityStates = new HashMap<>();
    private TextToSpeech textToSpeech;
    private UserProgressService userProgressService;
    private UserDTO user;

    public FlashcardGameManager(int maxWords, int wordsToPlay, List<Word> allPossibleWords, TextToSpeech textToSpeech, UserDTO user, Application application) {
        this.maxWords = maxWords;
        this.wordsToPlay = wordsToPlay;
        this.allPosibleWords = allPossibleWords;
        Collections.shuffle(this.allPosibleWords);
        // Inicializar Text-to-Speech
        this.textToSpeech = textToSpeech;
        this.user = user;
        userProgressService = UserProgressService.getInstance(application);
    }

    public Word getCurrentWord() {
        if (currentCardPosition >= allPosibleWords.size()) {
            return null;
        }
        return allPosibleWords.get(currentCardPosition);
    }

    public boolean hasNextWord() {
        return currentCardPosition < wordsToPlay ;
    }

    public boolean hasMoreWords(){
        return allPosibleWords != null && !allPosibleWords.isEmpty();
    }

    public void nextWord() {
        if (hasNextWord()) {
            currentCardPosition++;
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
            setScore(score+1);
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
        Collections.shuffle(allPosibleWords);
    }

    //This play the current word
    public void speakCurrentWord() {
        if (textToSpeech != null) {
            String text = getCurrentWord().getWord();
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    public void addLearnedWord() {
        UserProgressDTO userProgressDTO = new UserProgressDTO(0, getCurrentWord().getId(), user.getId(),0,0, ProgressType.WORD_LEARNED);
        userProgressService.insertUserProgress(userProgressDTO);
        this.user.setXp(this.user.getXp() + 1);
    }
}
