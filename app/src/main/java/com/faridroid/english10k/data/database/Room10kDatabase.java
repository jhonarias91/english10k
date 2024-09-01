package com.faridroid.english10k.data.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.faridroid.english10k.data.dao.CategoryDao;
import com.faridroid.english10k.data.dao.TestResultDao;
import com.faridroid.english10k.data.dao.UserDao;
import com.faridroid.english10k.data.dao.UserProgressDao;
import com.faridroid.english10k.data.dao.WordDao;
import com.faridroid.english10k.data.entity.Category;
import com.faridroid.english10k.data.entity.TestResult;
import com.faridroid.english10k.data.entity.User;
import com.faridroid.english10k.data.entity.UserProgress;
import com.faridroid.english10k.data.entity.Word;

@Database(entities = {Word.class, Category.class, UserProgress.class, TestResult.class, User.class}, version = 2, exportSchema = true)
public abstract class Room10kDatabase extends RoomDatabase {
    public abstract WordDao wordDao();
    public abstract CategoryDao categoryDao();
    public abstract UserProgressDao userProgressDao();
    public abstract TestResultDao testResultDao();
    public abstract UserDao userDao();

}