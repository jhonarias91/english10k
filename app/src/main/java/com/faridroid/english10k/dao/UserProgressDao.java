package com.faridroid.english10k.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.faridroid.english10k.entity.UserProgress;

import java.util.List;

@Dao
public interface UserProgressDao {
    @Insert
    void insertUserProgress(UserProgress userProgress);

    @Update
    void updateUserProgress(UserProgress userProgress);

    @Query("SELECT * FROM user_progress WHERE id = :id")
    UserProgress getUserProgressById(int id);

    @Query("DELETE FROM user_progress WHERE id = :id")
    void deleteUserProgress(int id);

    @Query("SELECT * FROM user_progress")
    List<UserProgress> getAllUserProgress();
}
