package com.faridroid.english10k.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.faridroid.english10k.data.dto.ProgressType;
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

    @Query("SELECT * FROM custom_words w WHERE NOT EXISTS (" +
            "SELECT 1 FROM user_custom_progress up WHERE up.custom_word_id = w.id " +
            "AND up.progress_type = :progressType) AND w.list_id = :listId")
    LiveData<List<CustomWord>> getWordsNotLearned(String listId, ProgressType progressType);

    @Query("SELECT * FROM custom_words w WHERE EXISTS (" +
            "SELECT 1 FROM user_custom_progress up WHERE up.custom_word_id = w.id " +
            "AND up.progress_type = :progressType) AND w.list_id = :listId")
    LiveData<List<CustomWord>> getWordsLearned(String listId, ProgressType progressType);
}
