package com.faridroid.english10k.data.dto;

public class CategoryDTO {
    private String id;

    private String name;  // The name that the user can modify

    private String originalName;  // The original name when it was download from internet

    private String userId;  // The ID of the user who owns this category

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public CategoryDTO() {
    }

    public CategoryDTO(String id, String userId, String name, String originalName) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.originalName = originalName;
    }

    public CategoryDTO(String userId, String name, String originalName) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.originalName = originalName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}