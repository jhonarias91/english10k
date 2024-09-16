package com.faridroid.english10k.data.dto;

public class UserCustomProgressDTO {

    private String id;

    private String customWordId;  // Relationship with the custom list

    private int progress;  // User's progress in the custom list

    private Long lastUpdated;  // Timestamp of the last update

    private ProgressType progressType; //1 -> word learned


    public UserCustomProgressDTO(String id, String customWordId, int progress, ProgressType progressType) {
        this.id = id;
        this.customWordId = customWordId;
        this.progress = progress;
        this.progressType = progressType;
    }

    public UserCustomProgressDTO(String listId, int progress, Long lastUpdated) {
        this.customWordId = listId;
        this.progress = progress;
        this.lastUpdated = lastUpdated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
