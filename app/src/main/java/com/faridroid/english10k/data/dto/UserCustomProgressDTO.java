package com.faridroid.english10k.data.dto;

public class UserCustomProgressDTO {

    private int id;

    private String listId;  // Relationship with the custom list

    private int progress;  // User's progress in the custom list

    private Long lastUpdated;  // Timestamp of the last update


    public UserCustomProgressDTO(int id, String listId, int progress, Long lastUpdated) {
        this.id = id;
        this.listId = listId;
        this.progress = progress;
        this.lastUpdated = lastUpdated;
    }

    public UserCustomProgressDTO(String listId, int progress, Long lastUpdated) {
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

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
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
