package com.faridroid.english10k.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.faridroid.english10k.data.entity.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insertCategory(Category category);

    @Update
    void updateCategory(Category category);

    @Query("SELECT * FROM categories WHERE id = :id")
    Category getCategoryById(int id);

    @Query("DELETE FROM categories WHERE id = :id")
    void deleteCategory(int id);

    @Query("SELECT * FROM categories")
    List<Category> getAllCategories();
}
