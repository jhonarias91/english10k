package com.faridroid.english10k.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_custom_progress",
        foreignKeys = {
                @ForeignKey(entity = CustomList.class,
                            parentColumns = "id",
                            childColumns = "list_id",
                            onDelete = ForeignKey.CASCADE)
        })
public class UserCustomProgress {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;  // Relationship with the user (can reference a User table)

    @ColumnInfo(name = "list_id")
    private int listId;  // Relationship with the custom list

    @ColumnInfo(name = "progress")
    private int progress;  // User's progress in the custom list

    @ColumnInfo(name = "last_updated")
    private Long lastUpdated;  // Timestamp of the last update

    public UserCustomProgress(int userId, int listId, int progress, Long lastUpdated) {
        this.userId = userId;
        this.listId = listId;
        this.progress = progress;
        this.lastUpdated = lastUpdated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
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
