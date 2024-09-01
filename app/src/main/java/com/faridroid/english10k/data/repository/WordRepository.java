package com.faridroid.english10k.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.faridroid.english10k.data.dao.WordDao;
import com.faridroid.english10k.data.database.DatabaseClient;
import com.faridroid.english10k.data.database.Room10kDatabase;
import com.faridroid.english10k.data.entity.Word;

import java.util.List;

public class WordRepository {
    private WordDao wordDao;

    private static WordRepository instance;

    public static WordRepository getInstance(Application application) {
        if (instance == null) {
            instance = new WordRepository(application);
        }
        return instance;
    }

    private WordRepository(Application application) {
        Room10kDatabase db = DatabaseClient.getDatabase(application.getApplicationContext());
        wordDao = db.wordDao();
    }

    public LiveData<List<Word>> getWordsByLimit(int limit) {
        return wordDao.getWordsByLimit(limit);
    }

    public LiveData<Integer> getTotalWords() {
        return wordDao.getTotalWords();
    }

}