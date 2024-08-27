package com.faridroid.english10k.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.faridroid.english10k.data.entity.Word;
import com.faridroid.english10k.data.repository.WordRepository;
import java.util.List;

public class WordViewModel extends AndroidViewModel {

    private WordRepository repository;


    public WordViewModel(Application application) {
        super(application);
        repository = new WordRepository(application);
    }

    public LiveData<List<Word>> getWordsByLimit(int limit) {
        return repository.getWordsByLimit(limit);
    }

    public LiveData<Integer> getTotalWords() {
        return repository.getTotalWords();
    }
}

