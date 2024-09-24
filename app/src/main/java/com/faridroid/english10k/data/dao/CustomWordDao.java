package com.faridroid.english10k.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.faridroid.english10k.data.entity.CustomWord;

import java.util.List;

@Dao
public interface CustomWordDao {

    @Insert
    void insertCustomWord(CustomWord customWord);

    @Update
    void updateCustomWord(CustomWord customWord);

    @Query("SELECT * FROM custom_words WHERE id = :id")
    CustomWord getCustomWordById(String id);

    @Query("SELECT * FROM custom_words WHERE list_id = :listId")
    LiveData<List<CustomWord>> getCustomWordsByList(String listId);

    @Transaction
    @Query("SELECT * FROM custom_words")
    LiveData<List<CustomWord>> getAllCustomWords();

    @Query("DELETE FROM custom_words WHERE id = :id")
    void deleteCustomWord(String id);

    @Query("DELETE FROM custom_words WHERE list_id = :currentListId")
    void deleteCustomList(String currentListId);
}
