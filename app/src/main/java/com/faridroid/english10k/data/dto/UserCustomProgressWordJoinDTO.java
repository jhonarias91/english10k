package com.faridroid.english10k.data.dto;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.faridroid.english10k.data.entity.CustomWord;
import com.faridroid.english10k.data.entity.UserCustomProgress;

public class UserCustomProgressWordJoinDTO {

    @Embedded
    private UserCustomProgress userProgress;

    @Relation(
            parentColumn = "custom_word_id",  // El nombre de la columna en UserProgress que referencia Word
            entityColumn = "id"   // La clave primaria en Word
    )
    private CustomWord word;

    public UserCustomProgress getUserProgress() {
        return userProgress;
    }

    public void setUserProgress(UserCustomProgress userProgress) {
        this.userProgress = userProgress;
    }

    public CustomWord getWord() {
        return word;
    }

    public void setWord(CustomWord word) {
        this.word = word;
    }
}
