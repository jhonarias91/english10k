package com.faridroid.english10k.data.dto;

public class CustomListDTO {

    private String id;

    private String name;  // The name that the user can modify

    private String originalName;  // The original name when the list is created or downloaded

    private String categoryId;  // Relationship with the category

    public CustomListDTO(String id, String name, String originalName, String categoryId) {
        this.id = id;
        this.name = name;
        this.originalName = originalName;
        this.categoryId = categoryId;
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
}
