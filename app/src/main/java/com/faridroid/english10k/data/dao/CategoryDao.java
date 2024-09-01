package com.faridroid.english10k.data.dao;

import androidx.lifecycle.LiveData;
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

    @Query("SELECT * FROM categories")
    LiveData<List<Category>> getAllCategories();

    @Query("DELETE FROM categories WHERE id = :id")
    void deleteCategory(int id);
}
