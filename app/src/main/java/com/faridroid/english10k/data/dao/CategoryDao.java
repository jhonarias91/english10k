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
    Long insertCategory(Category category);

    @Update
    void updateCategory(Category category);

    @Query("SELECT * FROM categories WHERE id = :id")
    Category getCategoryById(String id);

    @Query("SELECT * FROM categories WHERE user_id = :userId")
    LiveData<List<Category>> getAllCategories(String userId);

    @Query("DELETE FROM categories WHERE id = :id")
    void deleteCategory(String id);
}
