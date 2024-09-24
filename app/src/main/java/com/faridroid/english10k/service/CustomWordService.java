package com.faridroid.english10k.service;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.faridroid.english10k.data.dto.CustomWordDTO;
import com.faridroid.english10k.data.dto.interfaces.WordInterface;
import com.faridroid.english10k.data.entity.CustomWord;
import com.faridroid.english10k.data.repository.CustomWordRepository;

import java.util.ArrayList;
import java.util.List;

public class CustomWordService {

    private static CustomWordService instance;
    private CustomWordRepository customWordRepository;

    private CustomWordService(Application application) {
        customWordRepository = CustomWordRepository.getInstance(application);
    }

    public static CustomWordService getInstance(Application application) {
        if (instance == null) {
            instance = new CustomWordService(application);
        }
        return instance;
    }

    public LiveData<List<WordInterface>> getCustomWordsByList(String listId) {
        LiveData<List<CustomWord>> customWordsByList = customWordRepository.getCustomWordsByList(listId);

        return Transformations.map(customWordsByList, customWords -> {
            List<WordInterface> dtoList = new ArrayList<>();
            for (CustomWord customWord : customWords) {
                WordInterface dto = new CustomWordDTO(
                        customWord.getId(),
                        customWord.getListId(),
                        customWord.getWord(),
                        customWord.getSpanish()
                );
                dtoList.add(dto);
            }
            return dtoList;
        });
    }

    public void insertCustomWord(CustomWordDTO customWordDTO) {
        CustomWord customWord = new CustomWord(
                customWordDTO.getId(),
                customWordDTO.getListId(),
                customWordDTO.getWord(),
                customWordDTO.getSpanish()
        );
        customWordRepository.insertCustomWord(customWord);
    }

    public void updateCustomWord(CustomWordDTO customWordDTO) {
        CustomWord customWord = new CustomWord(
                customWordDTO.getId(),
                customWordDTO.getListId(),
                customWordDTO.getWord(),
                customWordDTO.getSpanish()
        );
        customWordRepository.updateCustomWord(customWord);
    }

    public void deleteCustomWord(String id) {
        customWordRepository.deleteCustomWord(id);
    }

    public void deleteCustomList(String currentListId) {
        customWordRepository.deleteCustomList(currentListId);
    }
}
