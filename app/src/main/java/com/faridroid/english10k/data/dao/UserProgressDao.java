package com.faridroid.english10k.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Transaction;
import androidx.room.Update;

import com.faridroid.english10k.data.dto.ProgressType;
import com.faridroid.english10k.data.dto.UserProgressWordJoinDTO;
import com.faridroid.english10k.data.entity.UserProgress;

import java.util.List;

@Dao
public interface UserProgressDao {
    @Insert
    void insertUserProgress(UserProgress userProgress);

    @Update
    void updateUserProgress(UserProgress userProgress);

    @Query("SELECT * FROM user_progress WHERE user_progress_id = :id")
    UserProgress getUserProgressById(int id);

    @Query("DELETE FROM user_progress WHERE user_progress_id = :id")
    void deleteUserProgress(int id);

    @Query("SELECT * FROM user_progress WHERE user_id = :userId")
    LiveData<List<UserProgress>> getAllUserProgress(String userId);

    @Query("SELECT * FROM user_progress WHERE user_id = :userId and progress_type = :progressType")
    LiveData<List<UserProgress>> getAllUserProgressByType(String userId, ProgressType progressType);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Transaction
    @Query("SELECT * FROM user_progress INNER JOIN words ON user_progress.word_id = words.id " +
            "WHERE user_progress.user_id = :userId AND user_progress.progress_type = :progressType " +
            "ORDER BY user_progress.user_progress_id DESC")
    LiveData<List<UserProgressWordJoinDTO>> listUserProgressWithWord(String userId, ProgressType progressType);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<UserProgress> userProgressBackup);

    @Query("SELECT * FROM user_progress")
    LiveData<List<UserProgress>> getAllUserProgress();

    @Query("SELECT * FROM user_progress")
    List<UserProgress> getAllUserProgressDirect();

    @Query("DELETE FROM user_progress WHERE word_id = :wordId AND progress_type = :progressType")
    void deleteUserProgressByWordIdAndProgressType(String wordId, ProgressType progressType);
}
