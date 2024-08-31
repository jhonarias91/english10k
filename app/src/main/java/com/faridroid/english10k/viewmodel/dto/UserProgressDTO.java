package com.faridroid.english10k.viewmodel.dto;

import androidx.room.Ignore;

import org.jetbrains.annotations.NotNull;

public class UserProgressDTO{

    private int id;
    private int wordId;
    private String userId;
    private int status;
    private long lastUpdated;
    private ProgressType progressType;

    // Constructors
    public UserProgressDTO() {}


    @Ignore
    public UserProgressDTO(int id, int wordId, String userId, int status, long lastUpdated, ProgressType progressType) {
        this.id = id;
        this.wordId = wordId;
        this.userId = userId;
        this.status = status;
        this.lastUpdated = lastUpdated;
        this.progressType = progressType;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    @NotNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NotNull String userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @NotNull
    public ProgressType getProgressType() {
        return progressType;
    }

    public void setProgressType(@NotNull ProgressType progressType) {
        this.progressType = progressType;
    }
}