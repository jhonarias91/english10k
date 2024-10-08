package com.faridroid.english10k.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.faridroid.english10k.data.dao.UserCustomProgressDao;
import com.faridroid.english10k.data.database.DatabaseClient;
import com.faridroid.english10k.data.database.Room10kDatabase;
import com.faridroid.english10k.data.dto.ProgressType;
import com.faridroid.english10k.data.dto.UserCustomProgressWordJoinDTO;
import com.faridroid.english10k.data.entity.UserCustomProgress;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserCustomProgressRepository {

    private static UserCustomProgressRepository instance;
    private UserCustomProgressDao dao;
    private ExecutorService executorService;

    private UserCustomProgressRepository(Application application) {
        Room10kDatabase db = DatabaseClient.getDatabase(application.getApplicationContext());
        dao = db.userCustomProgressDao();
        executorService = Executors.newFixedThreadPool(1);
    }

    public static synchronized UserCustomProgressRepository getInstance(Application application) {
        if (instance == null) {
            instance = new UserCustomProgressRepository(application);
        }
        return instance;
    }

    public LiveData<List<UserCustomProgress>> getUserCustomProgressByListIdAndProgressType(String listId,  ProgressType progressType) {
        return dao.getUserCustomProgressByListIdAndProgressType(listId, progressType);
    }

    public void insertUserCustomProgress(UserCustomProgress userCustomProgress) {
        executorService.execute(() -> dao.insertUserCustomProgress(userCustomProgress));
    }

    public void updateUserCustomProgress(UserCustomProgress userCustomProgress) {
        executorService.execute(() -> dao.updateUserCustomProgress(userCustomProgress));
    }

    public void deleteUserCustomProgress(String id) {
        executorService.execute(() -> dao.deleteUserCustomProgress(id));
    }

    public LiveData<List<UserCustomProgressWordJoinDTO>> listCustomUserProgressWithWordByListId(String listId,ProgressType progressType){
            return dao.listCustomUserProgressWithWordByListId(listId, progressType);
    }

    public void deleteUserCustomProgressByWordIdAndProgressType(String wordId, ProgressType progressType) {
        executorService.execute(() -> dao.deleteUserCustomProgressByWordIdAndProgressType(wordId, progressType));
    }
}
