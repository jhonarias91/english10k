package com.faridroid.english10k.data.repository;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

import com.faridroid.english10k.data.dao.UserProgressDao;
import com.faridroid.english10k.data.database.DatabaseClient;
import com.faridroid.english10k.data.database.Room10kDatabase;
import com.faridroid.english10k.data.dto.ProgressType;
import com.faridroid.english10k.data.dto.UserProgressWordJoinDTO;
import com.faridroid.english10k.data.entity.UserProgress;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserProgressRepository {

    public static UserProgressRepository instance;

    private UserProgressDao dao;
    private SharedPreferences sharedPreferences;
    private ExecutorService executorService;


    public static UserProgressRepository getInstance(Application application) {
        if (instance == null) {
            instance = new UserProgressRepository(application);
        }
        return instance;
    }

    private UserProgressRepository(Application application) {
        Room10kDatabase db = DatabaseClient.getDatabase(application.getApplicationContext());
        dao = db.userProgressDao();
        executorService = Executors.newFixedThreadPool(1);
        //sharedPreferences = application.getSharedPreferences("user_prefs", Application.MODE_PRIVATE);

    }

    public LiveData<List<UserProgress>> getAllUserProgress(String userId) {
        return dao.getAllUserProgress(userId);
    }

    public LiveData<List<UserProgress>> getAllUserProgressByType(String userId, ProgressType progressType) {
        return dao.getAllUserProgressByType(userId, progressType);
    }

    public LiveData<List<UserProgressWordJoinDTO>> listUserProgressWithWord(String userId, ProgressType progressType){
        return dao.listUserProgressWithWord(userId, progressType);
    }


    //delete userProgress by progressId
    public void deleteUserProgress(int id) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        executorService.execute(() -> {
            try {
                dao.deleteUserProgress(id);
                completableFuture.complete(null); // Complete normally
            } catch (Exception e) {
                completableFuture.completeExceptionally(e); // Complete with error
            }
        });
    }

    public void insertUserProgress(UserProgress userProgress) {

        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        executorService.execute(() -> {
            try {
                dao.insertUserProgress(userProgress);
                completableFuture.complete(null); // Complete normally
            } catch (Exception e) {
                completableFuture.completeExceptionally(e); // Complete with error
            }
        });
    }

    public void deleteUserProgressByWordIdAndProgressType(String wordId, ProgressType progressType) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        executorService.execute(() -> {
            try {
                dao.deleteUserProgressByWordIdAndProgressType(wordId, progressType);
                completableFuture.complete(null); // Complete normally
            } catch (Exception e) {
                completableFuture.completeExceptionally(e); // Complete with error
            }
        });
    }
}
