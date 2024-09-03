package com.faridroid.english10k.service;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.faridroid.english10k.data.dto.CustomListDTO;
import com.faridroid.english10k.data.entity.CustomList;
import com.faridroid.english10k.data.repository.CustomListRepository;

import java.util.ArrayList;
import java.util.List;

public class CustomListService {

    private static CustomListService instance;
    private CustomListRepository customListRepository;

    private CustomListService(Application application) {
        customListRepository = CustomListRepository.getInstance(application);
    }

    public static CustomListService getInstance(Application application) {
        if (instance == null) {
            instance = new CustomListService(application);
        }
        return instance;
    }

    public LiveData<List<CustomListDTO>> getListsByCategoryId(String categoryId) {
        LiveData<List<CustomList>> allCustomLists = customListRepository.getCustomListsByCategory(categoryId);

        return Transformations.map(allCustomLists, customLists -> {
            List<CustomListDTO> dtoList = new ArrayList<>();
            for (CustomList customList : customLists) {
                CustomListDTO dto = new CustomListDTO(
                        customList.getId(),
                        customList.getCategoryId(),
                        customList.getName(),
                        customList.getOriginalName()
                );
                dtoList.add(dto);
            }
            return dtoList;
        });
    }


    public void insertCustomList(CustomList customList) {
        customListRepository.insertCustomList(customList);
    }

    public void updateCustomList(CustomList customList) {
        customListRepository.updateCustomList(customList);
    }

    public void deleteCustomList(String id) {
        customListRepository.deleteCustomList(id);
    }

    public void insertDefaultCustomList(String name, String originalName, String categoryId) {
        CustomList customList = new CustomList(name, originalName, categoryId);
        customListRepository.insertCustomList(customList);
    }

    public LiveData<CustomListDTO> getListByNameAndCategoryId(String listName, String lastCategoryId) {
        LiveData<CustomList> listByNameAndCategoryId = customListRepository.getListByNameAndCategoryId(listName, lastCategoryId);

        return Transformations.map(listByNameAndCategoryId, customList -> {
            if (customList != null) {
                return new CustomListDTO(
                        customList.getId(),
                        customList.getName(),
                        customList.getOriginalName(),
                        customList.getCategoryId()
                );
            }
            return null;
        });
    }
}
