package com.faridroid.english10k.data.dto;

import androidx.room.Embedded;

import com.faridroid.english10k.data.entity.CustomWord;
import com.faridroid.english10k.data.entity.UserCustomProgress;

public class UserCustomProgressWordJoinDTO {

    @Embedded(prefix = "up_")
    private UserCustomProgress userProgress;

    @Embedded(prefix = "cw_")
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
