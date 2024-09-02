package com.faridroid.english10k.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.faridroid.english10k.data.dto.ProgressType;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "user_custom_progress",
        foreignKeys = {
                @ForeignKey(entity = CustomWord.class,
                            parentColumns = "id",
                            childColumns = "custom_word_id",
                            onDelete = ForeignKey.CASCADE)
        })
public class UserCustomProgress {

    @PrimaryKey
    @NotNull
    private String id;

    @ColumnInfo(name = "custom_word_id")
    @NotNull
    private String customWordId;  // Relationship with the custom word

    @ColumnInfo(name = "progress")
    private int progress;  // User's progress in the custom list

    @ColumnInfo(name = "last_updated")
    private Long lastUpdated;  // Timestamp of the last update

    @ColumnInfo(name = "progress_type", defaultValue = "1")
    @NotNull
    private ProgressType progressType; //1 -> word learned


    @NotNull
    public ProgressType getProgressType() {
        return progressType;
    }

    public void setProgressType(@NotNull ProgressType progressType) {
        this.progressType = progressType;
    }

    public UserCustomProgress(@NotNull String id, String customWordId, int progress, Long lastUpdated, ProgressType progressType) {
        this.id = id;
        this.customWordId = customWordId;
        this.progress = progress;
        this.lastUpdated = lastUpdated;
        this.progressType = progressType;
    }

    @Ignore
    public UserCustomProgress(String customWordId, int progress, Long lastUpdated, ProgressType progressType) {
        this.customWordId = customWordId;
        this.progress = progress;
        this.lastUpdated = lastUpdated;
        this.progressType = progressType;
    }

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    public String getCustomWordId() {
        return customWordId;
    }

    public void setCustomWordId(String customWordId) {
        this.customWordId = customWordId;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
