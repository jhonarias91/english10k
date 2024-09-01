package com.faridroid.english10k.data.repository;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.lifecycle.LiveData;

import com.faridroid.english10k.data.dao.UserDao;
import com.faridroid.english10k.data.database.DatabaseClient;
import com.faridroid.english10k.data.database.Room10kDatabase;
import com.faridroid.english10k.data.entity.User;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private UserDao dao;
    private SharedPreferences sharedPreferences;
    private ExecutorService executorService;
private static UserRepository instance;

    public static UserRepository getInstance(Application application) {
        if (instance == null) {
            instance = new UserRepository(application);
        }
        return instance;
    }


    private UserRepository(Application application) {
        Room10kDatabase db = DatabaseClient.getDatabase(application.getApplicationContext());
        dao = db.userDao();
        sharedPreferences = application.getSharedPreferences("user_prefs", Application.MODE_PRIVATE);
        executorService = Executors.newFixedThreadPool(1);
    }

    public LiveData<User> getUserById() {
        String id = sharedPreferences.getString("user_id", null);
        if (id == null) {
            // If no ID is found, load the most recent user and store ID
            LiveData<User> userLiveData = dao.getUserByCreatedAt();
            userLiveData.observeForever(user -> {
                if (user != null) {
                    sharedPreferences.edit().putString("user_id", user.getId()).apply();
                }
            });
            return userLiveData;
        } else {
            // ID found, return the corresponding user
            return dao.getUserById(id);
        }
    }

    public CompletableFuture<Void> insertUser(User user) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        executorService.execute(() -> {
            try {
                dao.insertUser(user);
                completableFuture.complete(null); // Complete normally
            } catch (Exception e) {
                completableFuture.completeExceptionally(e); // Handle exceptions
            }
        });
        return completableFuture;
    }

    public void updateUser(User user){
        dao.updateUser(user);
    }

    public CompletableFuture<Void> updateXp(String id, long xp){
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        executorService.execute(() -> {
            try {
                dao.updateXp(id, xp);
                completableFuture.complete(null); // Complete normally
            } catch (Exception e) {
                completableFuture.completeExceptionally(e); // Handle exceptions
            }
        });
        return completableFuture;
    }
}
