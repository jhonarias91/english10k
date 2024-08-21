package com.faridroid.english10k.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.faridroid.english10k.entity.Word;

import java.util.List;

@Dao
public interface WordDao {

    @Insert
    void insertWord(Word word);

    @Update
    void updateWord(Word word);

    @Query("SELECT * FROM words WHERE id = :id")
    Word getWordById(int id);

    @Query("SELECT * FROM words")
    List<Word> getAllWords();

    @Query("DELETE FROM words where id = :id")
    void deleteWord(int id) ;



}
