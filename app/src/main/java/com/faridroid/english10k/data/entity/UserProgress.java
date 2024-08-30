package com.faridroid.english10k.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.faridroid.english10k.viewmodel.dto.ProgressType;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "user_progress")
public class UserProgress {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int wordId;
    @NotNull
    private String userId;
    private int status;
    @ColumnInfo(name = "lastUpdated")
    private long lastUpdated;
    @ColumnInfo(name = "progressType", defaultValue = "1")
    @NotNull
    private ProgressType progressType; //1 -> word learned

    public UserProgress(int id, int wordId, @NotNull String userId, int status, long lastUpdated, @NotNull ProgressType progressType) {
        this.id = id;
        this.wordId = wordId;
        this.userId = userId;
        this.status = status;
        this.lastUpdated = lastUpdated;
        this.progressType = progressType;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

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

    public long getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }

    @NotNull
    public ProgressType getProgressType() {
        return progressType;
    }

    public void setProgressType(@NotNull ProgressType progressType) {
        this.progressType = progressType;
    }
}
