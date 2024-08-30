package com.faridroid.english10k.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.faridroid.english10k.data.entity.UserProgress;
import com.faridroid.english10k.data.entity.Word;
import com.faridroid.english10k.data.repository.WordRepository;
import com.faridroid.english10k.service.UserProgressService;
import com.faridroid.english10k.viewmodel.dto.UserProgressDTO;

import java.util.List;
import java.util.stream.Collectors;

public class WordViewModel extends AndroidViewModel {

    private WordRepository repository;
    private UserProgressViewModel userProgressViewModel;
    private UserProgressService userProgressService;

    public WordViewModel(Application application) {
        super(application);
        repository = WordRepository.getInstance(application);
        userProgressService = UserProgressService.getInstance(application);
    }

    public LiveData<List<Word>> getWordsByLimit(int limit, String userId) {
        // TODO: 29/08/2024 change to limit 
        LiveData<List<Word>> wordsByLimit = repository.getWordsByLimit(5);
        LiveData<List<UserProgressDTO>> allUserProgress = userProgressService.getAllUserProgress(userId);

        //if the word on wordsByLimit is already learned, remove it from the list

        return Transformations.switchMap(allUserProgress, userProgressList ->
                Transformations.map(wordsByLimit, words ->
                        words.stream()
                                .filter(word -> userProgressList.stream().noneMatch(progress -> progress.getWordId() == word.getId()))
                                .collect(Collectors.toList())
                )
        );
    }

    //todo: here need logic to exclude the one already learned
    public LiveData<Integer> getTotalWords() {
        return repository.getTotalWords();
    }
}

