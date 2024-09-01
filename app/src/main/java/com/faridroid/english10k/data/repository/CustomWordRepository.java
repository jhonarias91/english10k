package com.faridroid.english10k.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.faridroid.english10k.data.dao.CustomWordDao;
import com.faridroid.english10k.data.database.DatabaseClient;
import com.faridroid.english10k.data.database.Room10kDatabase;
import com.faridroid.english10k.data.entity.CustomWord;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomWordRepository {

    private static CustomWordRepository instance;
    private CustomWordDao dao;
    private ExecutorService executorService;

    private CustomWordRepository(Application application) {
        Room10kDatabase db = DatabaseClient.getDatabase(application.getApplicationContext());
        dao = db.customWordDao();
        executorService = Executors.newFixedThreadPool(1);
    }

    public static synchronized CustomWordRepository getInstance(Application application) {
        if (instance == null) {
            instance = new CustomWordRepository(application);
        }
        return instance;
    }

    public LiveData<List<CustomWord>> getAllCustomWords() {
        return dao.getAllCustomWords();
    }

    public LiveData<List<CustomWord>> getCustomWordsByList(int listId) {
        return dao.getCustomWordsByList(listId);
    }

    public void insertCustomWord(CustomWord customWord) {
        executorService.execute(() -> dao.insertCustomWord(customWord));
    }

    public void updateCustomWord(CustomWord customWord) {
        executorService.execute(() -> dao.updateCustomWord(customWord));
    }

    public void deleteCustomWord(int id) {
        executorService.execute(() -> dao.deleteCustomWord(id));
    }
}
