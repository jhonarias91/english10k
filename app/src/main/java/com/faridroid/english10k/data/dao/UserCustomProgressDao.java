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
    UserCustomProgress getUserCustomProgressById(String id);

    @Transaction
    @Query("SELECT * FROM user_custom_progress where custom_word_id = :customWordId")
    LiveData<List<UserCustomProgress>> getAllUserCustomProgressByCustomWordId(String customWordId);

    @Query("DELETE FROM user_custom_progress WHERE id = :id")
    void deleteUserCustomProgress(String id);
}
