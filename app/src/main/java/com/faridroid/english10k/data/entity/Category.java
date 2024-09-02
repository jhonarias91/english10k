package com.faridroid.english10k.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "categories")
public class Category {

    @PrimaryKey
    @NotNull
    private String id;

    @ColumnInfo(name = "user_id")
    private String userId;  // The ID of the user who owns this category

    @ColumnInfo(name = "name")
    private String name;  // The name that the user can modify

    @ColumnInfo(name = "original_name")
    private String originalName;  // The original name when the category is downloaded

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Ignore
    public Category(String userId, String name, String originalName) {
        this.userId = userId;
        this.name = name;
        this.originalName = originalName;
    }

    public Category(@NotNull String id,String userId, String name, String originalName) {
        this.id = id;
        this.name = name;
        this.originalName = originalName;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }
}