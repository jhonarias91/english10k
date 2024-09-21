package com.faridroid.english10k.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
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

    @Query("DELETE FROM user_custom_progress WHERE custom_word_id = :wordId AND progress_type = :progressType")
    void deleteUserCustomProgressByWordIdAndProgressType(String wordId, ProgressType progressType);

    @Transaction
    @Query("SELECT up.* FROM user_custom_progress AS up " +
            "INNER JOIN custom_words AS cw ON up.custom_word_id = cw.id " +
            "WHERE cw.list_id = :listId AND up.progress_type = :progressType")
    LiveData<List<UserCustomProgress>> getUserCustomProgressByListIdAndProgressType(
            String listId, ProgressType progressType);

    /**
     * Get the user progress for a custom list by the user id and the progress type
     * */
    @Query("SELECT " +
            "up.id AS up_id, " +
            "up.custom_word_id AS up_custom_word_id, " +
            "up.progress AS up_progress, " +
            "up.last_updated AS up_last_updated, " +
            "up.progress_type AS up_progress_type, " +
            "cw.id AS cw_id, " +
            "cw.list_id AS cw_list_id, " +
            "cw.word AS cw_word, " +
            "cw.spanish AS cw_spanish " +
            "FROM user_custom_progress AS up " +
            "INNER JOIN custom_words AS cw ON up.custom_word_id = cw.id " +
            "WHERE up.progress_type = :progressType " +
            "AND cw.list_id = :listId " +
            "ORDER BY up.last_updated DESC")
    LiveData<List<UserCustomProgressWordJoinDTO>> listCustomUserProgressWithWordByListId(
            String listId, ProgressType progressType);
}
