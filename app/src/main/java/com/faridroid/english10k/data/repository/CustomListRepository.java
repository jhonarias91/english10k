package com.faridroid.english10k.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.faridroid.english10k.data.dao.CustomListDao;
import com.faridroid.english10k.data.database.DatabaseClient;
import com.faridroid.english10k.data.database.Room10kDatabase;
import com.faridroid.english10k.data.entity.CustomList;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomListRepository {

    private static CustomListRepository instance;
    private CustomListDao dao;
    private ExecutorService executorService;

    private CustomListRepository(Application application) {
        Room10kDatabase db = DatabaseClient.getDatabase(application.getApplicationContext());
        dao = db.customListDao();
        executorService = Executors.newFixedThreadPool(1);
    }

    public static synchronized CustomListRepository getInstance(Application application) {
        if (instance == null) {
            instance = new CustomListRepository(application);
        }
        return instance;
    }

    public LiveData<List<CustomList>> getAllCustomLists() {
        return dao.getAllCustomLists();
    }

    public LiveData<List<CustomList>> getCustomListsByCategory(int categoryId) {
        return dao.getCustomListsByCategory(categoryId);
    }

    public void insertCustomList(CustomList customList) {
        executorService.execute(() -> dao.insertCustomList(customList));
    }

    public void updateCustomList(CustomList customList) {
        executorService.execute(() -> dao.updateCustomList(customList));
    }

    public void deleteCustomList(int id) {
        executorService.execute(() -> dao.deleteCustomList(id));
    }
}
