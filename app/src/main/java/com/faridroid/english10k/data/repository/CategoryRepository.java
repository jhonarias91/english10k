package com.faridroid.english10k.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.faridroid.english10k.data.dao.CategoryDao;
import com.faridroid.english10k.data.database.DatabaseClient;
import com.faridroid.english10k.data.database.Room10kDatabase;
import com.faridroid.english10k.data.entity.Category;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoryRepository {

    private static CategoryRepository instance;
    private CategoryDao dao;
    private ExecutorService executorService;

    private CategoryRepository(Application application) {
        Room10kDatabase db = DatabaseClient.getDatabase(application.getApplicationContext());
        dao = db.categoryDao();
        executorService = Executors.newFixedThreadPool(1);
    }

    public static synchronized CategoryRepository getInstance(Application application) {
        if (instance == null) {
            instance = new CategoryRepository(application);
        }
        return instance;
    }

    public LiveData<List<Category>> getAllCategories() {
        return dao.getAllCategories();
    }

    public void insertCategory(Category category) {
        executorService.execute(() -> dao.insertCategory(category));
    }

    public void updateCategory(Category category) {
        executorService.execute(() -> dao.updateCategory(category));
    }

    public void deleteCategory(int id) {
        executorService.execute(() -> dao.deleteCategory(id));
    }
}
