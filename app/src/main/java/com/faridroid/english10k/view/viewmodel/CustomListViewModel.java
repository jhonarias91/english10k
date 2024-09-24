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
                customListDTO.getCategoryId(),
                customListDTO.getName(),
                customListDTO.getOriginalName()
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

    public void deleteCustomList(String listId) {
        customListService.deleteCustomList(listId);
    }

    public LiveData<CustomListDTO> getCustomListByCategoryIdAndName(String lastCategoryId, String listName) {
        return customListService.getListByNameAndCategoryId(lastCategoryId,listName);
    }

}
