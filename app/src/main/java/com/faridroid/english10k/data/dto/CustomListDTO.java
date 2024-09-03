package com.faridroid.english10k.data.dto;

public class CustomListDTO {

    private String id;

    private String categoryId;  // Relationship with the category

    private String name;  // The name that the user can modify

    private String originalName;  // The original name when the list is created or downloaded

    public CustomListDTO(String id, String categoryId, String name, String originalName) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.originalName = originalName;
    }

    public CustomListDTO() {
    }

    public CustomListDTO(String name, String originalName, String categoryId) {
        this.name = name;
        this.originalName = originalName;
        this.categoryId = categoryId;
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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return name;
    }
}
