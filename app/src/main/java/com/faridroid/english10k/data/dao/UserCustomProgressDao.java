package com.faridroid.english10k.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.faridroid.english10k.data.entity.UserCustomProgress;

import java.util.List;

@Dao
public interface UserCustomProgressDao {

    @Insert
    void insertUserCustomProgress(UserCustomProgress userCustomProgress);

    @Update
    void updateUserCustomProgress(UserCustomProgress userCustomProgress);

    @Query("SELECT * FROM user_custom_progress WHERE id = :id")
    UserCustomProgress getUserCustomProgressById(int id);

    @Query("SELECT * FROM user_custom_progress WHERE user_id = :userId AND list_id = :listId")
    LiveData<List<UserCustomProgress>> getUserCustomProgressByUserAndList(int userId, int listId);

    @Transaction
    @Query("SELECT * FROM user_custom_progress")
    LiveData<List<UserCustomProgress>> getAllUserCustomProgress();

    @Query("DELETE FROM user_custom_progress WHERE id = :id")
    void deleteUserCustomProgress(int id);
}
