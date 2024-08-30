package com.faridroid.english10k.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.faridroid.english10k.data.entity.Word;

import java.util.List;

@Dao
public interface WordDao {

    @Query("SELECT * FROM words")
    LiveData<List<Word>> getAllWords();

    @Insert
    void insertWord(Word word);

    @Update
    void updateWord(Word word);

    @Query("DELETE FROM words WHERE id = :id")
    void deleteWord(int id);

    @Query("SELECT * FROM words ORDER BY range ASC LIMIT :limit")
    LiveData<List<Word>> getWordsByLimit(int limit);

    @Query("SELECT COUNT(*) FROM words")
    LiveData<Integer> getTotalWords();
}

