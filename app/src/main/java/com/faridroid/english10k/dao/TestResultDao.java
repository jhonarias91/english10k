package com.faridroid.english10k.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.faridroid.english10k.entity.TestResult;

import java.util.List;

@Dao
public interface TestResultDao {
    @Insert
    void insertTestResult(TestResult testResult);

    @Update
    void updateTestResult(TestResult testResult);

    @Query("SELECT * FROM test_results WHERE id = :id")
    TestResult getTestResultById(int id);

    @Query("DELETE FROM test_results WHERE id = :id")
    void deleteTestResult(int id);

    @Query("SELECT * FROM test_results")
    List<TestResult> getAllTestResults();
}
