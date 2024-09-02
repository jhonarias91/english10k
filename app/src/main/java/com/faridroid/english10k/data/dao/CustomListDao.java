package com.faridroid.english10k.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.faridroid.english10k.data.entity.CustomList;

import java.util.List;

@Dao
public interface CustomListDao {

    @Insert
    void insertCustomList(CustomList customList);

    @Update
    void updateCustomList(CustomList customList);

    @Query("SELECT * FROM custom_lists WHERE id = :id")
    CustomList getCustomListById(String id);

    @Query("SELECT * FROM custom_lists WHERE category_id = :categoryId")
    LiveData<List<CustomList>> getCustomListsByCategory(String categoryId);


    @Query("DELETE FROM custom_lists WHERE id = :id")
    void deleteCustomList(String id);

    @Query("SELECT * FROM custom_lists WHERE name = :listName AND category_id = :lastCategoryId")
    LiveData<CustomList> getListByNameAndCategoryId(String listName, String lastCategoryId);
}
