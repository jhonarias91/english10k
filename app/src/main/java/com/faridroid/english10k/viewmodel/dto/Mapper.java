package com.faridroid.english10k.viewmodel.dto;

import com.faridroid.english10k.data.entity.UserProgress;
import com.faridroid.english10k.data.entity.Word;

public class Mapper {
    
    public static UserProgressDTO mapToUserProgressDTO(UserProgress userProgress) {
        return new UserProgressDTO(
            userProgress.getUserProgressId(),
            userProgress.getWordId(),
            userProgress.getUserId(),
            userProgress.getStatus(),
            userProgress.getUpdated(),
            userProgress.getProgressType()
        );
    }

    public static WordDTO mapToWordDTO(Word word) {
        return new WordDTO(
            word.getId(),
            word.getWord(),
            word.getSpanish(),
            word.getRange(),
            word.getUpdated()
        );
    }
   }
