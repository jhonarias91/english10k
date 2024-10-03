package com.faridroid.english10k.view.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.faridroid.english10k.data.dto.ProgressType;
import com.faridroid.english10k.data.dto.WordDTO;
import com.faridroid.english10k.data.dto.interfaces.WordInterface;
import com.faridroid.english10k.data.enums.WordsGameTypeEnum;
import com.faridroid.english10k.data.repository.WordRepository;
import com.faridroid.english10k.service.UserProgressService;
import com.faridroid.english10k.service.WordService;

import java.util.ArrayList;
import java.util.List;

public class WordViewModel extends AndroidViewModel {

    private WordRepository repository;
    private UserProgressViewModel userProgressViewModel;
    private UserProgressService userProgressService;
    private WordService wordService;

    public WordViewModel(Application application) {
        super(application);
        repository = WordRepository.getInstance(application);
        userProgressService = UserProgressService.getInstance(application);
        wordService = WordService.getInstance(application);
    }

    public LiveData<List<WordInterface>> getWordsByLimit(int limit, String userId, WordsGameTypeEnum type) {
        if (type.equals(WordsGameTypeEnum.TO_LEARN)) {
            LiveData<List<WordDTO>> wordsNotLearned = wordService.getWordsNotLearned(userId, ProgressType.WORD_LEARNED, limit);
            return Transformations.map(wordsNotLearned, words -> new ArrayList<>(words));
        } else if (type.equals(WordsGameTypeEnum.LEARNED)) {
            LiveData<List<WordDTO>> wordsLearned = wordService.getWordsLearned(userId, ProgressType.WORD_LEARNED, limit);
            return Transformations.map(wordsLearned, words -> new ArrayList<>(words));
        } else {
            return new MutableLiveData<>(new ArrayList<>());
        }
    }

    //todo: here check if needed to exclude the one already learned
    public LiveData<Integer> getTotalWords() {
        return repository.getTotalWords();
    }
}

