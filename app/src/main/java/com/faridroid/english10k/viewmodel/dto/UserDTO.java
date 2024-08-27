package com.faridroid.english10k.viewmodel.dto;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;


public class UserDTO implements Serializable {

    private String id;
    private String username;
    private String email;
    private Long createdAt;
    private Long xp;

    public UserDTO(String id, String username, String email, Long createdAt, Long xp) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
        this.xp = xp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getXp() {
        return xp;
    }

    public void setXp(Long xp) {
        this.xp = xp;
    }
}
