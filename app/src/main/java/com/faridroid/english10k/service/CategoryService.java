package com.faridroid.english10k.service;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.faridroid.english10k.data.dto.CategoryDTO;
import com.faridroid.english10k.data.entity.Category;
import com.faridroid.english10k.data.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public LiveData<CategoryDTO> getCategoryByName(String userId, String categoryName) {
        LiveData<Category> defaultUserCategory = categoryRepository.getCategoryByName(userId, categoryName);

        return Transformations.map(defaultUserCategory, category -> {
            if (category != null) {
                return new CategoryDTO(
                        category.getId(),
                        category.getUserId(),
                        category.getName(),
                        category.getOriginalName()
                );
            }
            return null;
        });
    }

    public LiveData<CategoryDTO> getCategoryById(String categoryId) {
        LiveData<Category> defaultUserCategory = categoryRepository.getCategoryById(categoryId);

        return Transformations.map(defaultUserCategory, category -> {
            if (category != null) {
                return new CategoryDTO(
                        category.getId(),
                        category.getUserId(),
                        category.getName(),
                        category.getOriginalName()
                );
            }
            return null;
        });
    }

    public LiveData<CategoryDTO> getOrCreateCategoryByName(String userId, String categoryName) {
        // Find the defaultCategory by userId.
        LiveData<CategoryDTO> defaultCategory = getCategoryByName(userId, categoryName);

        // Use MediatorLiveData to monitor changes and update if necessary
        MediatorLiveData<CategoryDTO> result = new MediatorLiveData<>();

        result.addSource(defaultCategory, existingCategory -> {
            if (existingCategory  == null ) {
                // If there are no defaultCategoryDTO, create the default category.
                CategoryDTO defaultCategoryDTO = new CategoryDTO(UUID.randomUUID().toString(), userId, categoryName, null);
                insertCategory(defaultCategoryDTO);
                result.postValue(defaultCategoryDTO);
            } else {
                // Otherwise, just pass the existing defaultCategory to the result
                result.setValue(existingCategory );
            }
        });

        // Return the MediatorLiveData which holds the final result
        return result;
    }

}
