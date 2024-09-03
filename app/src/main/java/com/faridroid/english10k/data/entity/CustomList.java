package com.faridroid.english10k.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "custom_lists",
        foreignKeys = @ForeignKey(entity = Category.class,
                parentColumns = "id",
                childColumns = "category_id",
                onDelete = ForeignKey.CASCADE))
public class CustomList {

    @PrimaryKey
    @NotNull
    private String id;

    @ColumnInfo(name = "name")
    private String name;  // The name that the user can modify

    @ColumnInfo(name = "original_name")
    private String originalName;  // The original name when the list is created or downloaded

    @ColumnInfo(name = "category_id")
    private String categoryId;  // Relationship with the category

    @Ignore
    public CustomList(String name, String originalName, String categoryId) {
        this.name = name;
        this.originalName = originalName;
        this.categoryId = categoryId;
    }

    public CustomList(@NotNull String id,  String categoryId,String name, String originalName) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.originalName = originalName;
    }

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
