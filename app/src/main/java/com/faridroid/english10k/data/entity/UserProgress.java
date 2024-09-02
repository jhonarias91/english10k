package com.faridroid.english10k.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.faridroid.english10k.data.dto.ProgressType;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "user_progress")
public class UserProgress {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_progress_id")
    private int userProgressId;
    @ColumnInfo(name = "word_id")
    private int wordId;
    @NotNull
    @ColumnInfo(name = "user_id")
    private String userId;
    private int status;
    @ColumnInfo(name = "updated")
    private Long updated;
    @ColumnInfo(name = "progress_type", defaultValue = "1")
    @NotNull
    private ProgressType progressType; //1 -> word learned

    public UserProgress(int userProgressId, int wordId, @NotNull String userId, int status, Long updated, @NotNull ProgressType progressType) {
        this.userProgressId = userProgressId;
        this.wordId = wordId;
        this.userId = userId;
        this.status = status;
        this.updated = updated;
        this.progressType = progressType;
    }

    // Getters y Setters

    public int getUserProgressId() {
        return userProgressId;
    }

    public void setUserProgressId(int userProgressId) {
        this.userProgressId = userProgressId;
    }

    public int getWordId() { return wordId; }
    public void setWordId(int wordId) { this.wordId = wordId; }

    @NotNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NotNull String userId) {
        this.userId = userId;
    }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    @NotNull
    public ProgressType getProgressType() {
        return progressType;
    }

    public void setProgressType(@NotNull ProgressType progressType) {
        this.progressType = progressType;
    }
}
