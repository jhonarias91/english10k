package com.faridroid.english10k.data.dto;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.faridroid.english10k.data.entity.UserProgress;
import com.faridroid.english10k.data.entity.Word;

public class UserProgressWordJoinDTO {

    @Embedded
    private UserProgress userProgress;

    @Relation(
            parentColumn = "word_id",  // El nombre de la columna en UserProgress que referencia Word
            entityColumn = "id"   // La clave primaria en Word
    )
    private Word word;

    public UserProgress getUserProgress() {
        return userProgress;
    }

    public void setUserProgress(UserProgress userProgress) {
        this.userProgress = userProgress;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }
}
