package com.faridroid.english10k.dao.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_progress")
public class UserProgress {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int wordId;
    private int userId;
    private int status;
    private long lastReviewed;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getWordId() { return wordId; }
    public void setWordId(int wordId) { this.wordId = wordId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public long getLastReviewed() { return lastReviewed; }
    public void setLastReviewed(long lastReviewed) { this.lastReviewed = lastReviewed; }
}
