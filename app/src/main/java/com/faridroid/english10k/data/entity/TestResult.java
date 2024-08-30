package com.faridroid.english10k.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "test_results")
public class TestResult {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private int correctAnswers;
    private int totalQuestions;
    private long testDate;

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

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public long getTestDate() {
        return testDate;
    }

    public void setTestDate(long testDate) {
        this.testDate = testDate;
    }
}
