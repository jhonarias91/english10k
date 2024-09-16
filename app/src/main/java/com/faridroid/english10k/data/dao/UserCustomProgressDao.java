package com.faridroid.english10k.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Transaction;
import androidx.room.Update;

import com.faridroid.english10k.data.dto.ProgressType;
import com.faridroid.english10k.data.dto.UserCustomProgressWordJoinDTO;
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
    @Query("SELECT * FROM user_custom_progress where custom_word_id = :customWordId and progress_type = :progressType")
    LiveData<List<UserCustomProgress>> getAllUserCustomProgressByCustomWordId(String customWordId, ProgressType progressType);

    @Query("DELETE FROM user_custom_progress WHERE id = :id")
    void deleteUserCustomProgress(String id);

    /**
     * Get the user progress for a custom list by the user id and the progress type
     * */
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Transaction
    @Query("SELECT up.*, cw.* " + // Select all fields from user_progress (up) and custom_words (cw)
            "FROM user_custom_progress AS up " +
            "INNER JOIN custom_words AS cw ON up.custom_word_id = cw.id " + // Use the foreign key column
            "WHERE up.progress_type = :progressType " +
            "AND up.id = :userId " +
            "AND cw.list_id = :listId " +
            "ORDER BY up.last_updated DESC")
    LiveData<List<UserCustomProgressWordJoinDTO>> listCustomUserProgressWithWordByListId(String userId, String listId, ProgressType progressType);
}
