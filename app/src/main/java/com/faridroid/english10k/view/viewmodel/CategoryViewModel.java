package com.faridroid.english10k.view.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.faridroid.english10k.data.dto.CategoryDTO;
import com.faridroid.english10k.data.entity.Category;
import com.faridroid.english10k.service.CategoryService;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private CategoryService categoryService;
    private String userId;

    public CategoryViewModel(Application application) {
        super(application);
        categoryService = CategoryService.getInstance(application);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LiveData<CategoryDTO> getCategoryById(String categoryId) {
        return categoryService.getCategoryById(categoryId);
    }
    // Método para obtener todas las categorías del usuario
    public LiveData<List<CategoryDTO>> getAllCategories(String userId) {
       return categoryService.getAllCategories(userId);
    }

    public LiveData<CategoryDTO> getDefaultCategory(String userId) {
        return categoryService.getCategoryByName(userId, "Default");
    }

    public void insertCategory(CategoryDTO categoryDTO) {
        categoryService.insertCategory(categoryDTO);
    }

    public void updateCategory(CategoryDTO categoryDTO) {
        Category category = new Category(
                categoryDTO.getId(),
                categoryDTO.getUserId(),
                categoryDTO.getName(),
                categoryDTO.getOriginalName()
        );
        categoryService.updateCategory(category);
    }

    public void deleteCategory(String categoryId) {
        categoryService.deleteCategory(categoryId);
    }

    public LiveData<CategoryDTO> getOrCreateDefaultCategory() {
        return categoryService.getOrCreateCategoryByName(userId, "Default");
    }
}
