package com.faridroid.english10k.service;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.faridroid.english10k.data.dto.WordDTO;
import com.faridroid.english10k.data.entity.Word;
import com.faridroid.english10k.data.repository.WordRepository;

import java.util.ArrayList;
import java.util.List;

public class WordService {

    private static WordService instance;
    private WordRepository wordRepository;

    private WordService(Application application) {
        wordRepository = WordRepository.getInstance(application);
    }

    public static WordService getInstance(Application application) {
        if (instance == null) {
            instance = new WordService(application);
        }
        return instance;
    }


    public LiveData<List<WordDTO>> getWordsByLimit(int limit) {
        LiveData<List<Word>> wordsByLimit = wordRepository.getWordsByLimit(limit);

        return Transformations.map(wordsByLimit, words -> {
            List<WordDTO> dtoList = new ArrayList<>();
            for (Word word : words) {
                WordDTO dto = new WordDTO(
                        word.getId(),
                        word.getWord(),
                        word.getSpanish());
                dtoList.add(dto);
            }
            return dtoList;
        });
    }
    }
