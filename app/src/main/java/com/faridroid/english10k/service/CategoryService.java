package com.faridroid.english10k.service;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.faridroid.english10k.data.dto.CategoryDTO;
import com.faridroid.english10k.data.entity.Category;
import com.faridroid.english10k.data.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

public class CategoryService {

    private static CategoryService instance;
    private CategoryRepository categoryRepository;

    private CategoryService(Application application) {
        categoryRepository = CategoryRepository.getInstance(application);
    }

    public static CategoryService getInstance(Application application) {
        if (instance == null) {
            instance = new CategoryService(application);
        }
        return instance;
    }

    public LiveData<List<CategoryDTO>> getAllCategories(String userId) {
        LiveData<List<Category>> allCategories = categoryRepository.getAllCategories(userId);

        return Transformations.map(allCategories, categories -> {
            List<CategoryDTO> dtoList = new ArrayList<>();
            for (Category category : categories) {
                CategoryDTO dto = new CategoryDTO(
                        category.getId(),
                        category.getUserId(),
                        category.getName(),
                        category.getOriginalName()
                );
                dtoList.add(dto);
            }
            return dtoList;
        });
    }

    public void insertCategory(CategoryDTO categoryDTO) {
        Category category = new Category(
                categoryDTO.getId(),
                categoryDTO.getUserId(),
                categoryDTO.getName(),
                categoryDTO.getOriginalName()
        );
        categoryRepository.insertCategory(category);
    }

    public void updateCategory(Category category) {
        categoryRepository.updateCategory(category);
    }

    public void deleteCategory(String id) {
        categoryRepository.deleteCategory(id);
    }

    public void insertDefaultCategory(String userId, String name, String originalName) {
        Category category = new Category(userId, name, originalName);
        categoryRepository.insertCategory(category);
    }
}
