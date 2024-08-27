package com.faridroid.english10k.game;

import com.faridroid.english10k.data.entity.Word;

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

    public FlashcardGameManager(int maxWords, int wordsToPlay, List<Word> allPosibleWords) {
        this.maxWords = maxWords;
        this.wordsToPlay = wordsToPlay;
        this.allPosibleWords = allPosibleWords;
        Collections.shuffle(this.allPosibleWords);
    }

    public Word getCurrentWord() {
        return allPosibleWords.get(currentCardPosition);
    }

    public boolean hasNextWord() {
        return currentCardPosition < wordsToPlay ;
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
            score++;
        }
        boolean isFrontVisible = cardVisibilityStates.getOrDefault(currentCardPosition, true);
        cardVisibilityStates.put(currentCardPosition, !isFrontVisible);
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
}
