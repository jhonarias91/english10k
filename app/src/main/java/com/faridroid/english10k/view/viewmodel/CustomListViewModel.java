package com.faridroid.english10k.view.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.faridroid.english10k.data.dto.CustomListDTO;
import com.faridroid.english10k.data.entity.CustomList;
import com.faridroid.english10k.service.CustomListService;

import java.util.List;

public class CustomListViewModel extends AndroidViewModel {

    private CustomListService customListService;

    public CustomListViewModel(Application application) {
        super(application);
        customListService = CustomListService.getInstance(application);
    }

    // Método para obtener todas las categorías del usuario
    public LiveData<List<CustomListDTO>> getListsByCategoryId(String categoryId) {
       return customListService.getListsByCategoryId(categoryId);
    }
    public void insertCustomList(CustomListDTO customListDTO) {
        customListService.insertCustomList(new CustomList(
                customListDTO.getId(),
                customListDTO.getName(),
                customListDTO.getOriginalName(),
                customListDTO.getCategoryId()
        ));
    }

    public void updateCustomList(CustomListDTO categoryDTO) {
        CustomList category = new CustomList(
                categoryDTO.getName(),
                categoryDTO.getOriginalName(),
                categoryDTO.getCategoryId()
        );
        category.setId(categoryDTO.getId());
        customListService.updateCustomList(category);
    }

    public void delete(String categoryId) {
        customListService.deleteCustomList(categoryId);
    }

    public LiveData<CustomListDTO> getListByNameAndCategoryId(String listName, String lastCategoryId) {
        return customListService.getListByNameAndCategoryId(listName, lastCategoryId);
    }

}
