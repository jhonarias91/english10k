package com.faridroid.english10k.view.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.faridroid.english10k.data.dto.CustomWordDTO;
import com.faridroid.english10k.data.dto.interfaces.WordInterface;
import com.faridroid.english10k.data.entity.CustomWord;
import com.faridroid.english10k.service.CustomWordService;

import java.util.List;

public class CustomWordViewModel extends AndroidViewModel {

    private CustomWordService customWordService;

    public CustomWordViewModel(Application application) {
        super(application);
        customWordService = CustomWordService.getInstance(application);
    }

    public LiveData<List<WordInterface>> getCustomWordsByList(String listId) {
        return customWordService.getCustomWordsByList(listId);
    }

    public void insertCustomWord(CustomWordDTO customWordDTO) {
        customWordService.insertCustomWord(customWordDTO);
    }

    public void updateCustomWord(CustomWordDTO customWordDTO) {
        customWordService.updateCustomWord(customWordDTO);
    }

    public void deleteCustomWord(String id) {
        customWordService.deleteCustomWord(id);
    }

    private CustomWord mapDTOToEntity(CustomWordDTO dto) {
        return new CustomWord(dto.getId(), dto.getListId(), dto.getWord(), dto.getSpanish());
    }

    private CustomWordDTO mapEntityToDTO(CustomWord entity) {
        return new CustomWordDTO(entity.getId(), entity.getListId(), entity.getWord(), entity.getSpanish());
    }
}
