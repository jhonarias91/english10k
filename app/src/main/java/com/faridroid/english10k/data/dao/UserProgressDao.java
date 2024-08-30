package com.faridroid.english10k.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.faridroid.english10k.data.entity.User;
import com.faridroid.english10k.data.entity.UserProgress;
import com.faridroid.english10k.viewmodel.dto.ProgressType;

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

    @Query("SELECT * FROM user_progress WHERE userId = :userId")
    LiveData<List<UserProgress>> getAllUserProgress(String userId);

    @Query("SELECT * FROM user_progress WHERE userId = :userId and progressType = :progressType")
    LiveData<List<UserProgress>> getAllUserProgressByType(String userId, ProgressType progressType);

}
