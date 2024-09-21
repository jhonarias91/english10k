package com.faridroid.english10k.view.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.faridroid.english10k.data.dto.ProgressType;
import com.faridroid.english10k.data.dto.UserProgressDTO;
import com.faridroid.english10k.data.dto.WordDTO;
import com.faridroid.english10k.data.dto.interfaces.WordInterface;
import com.faridroid.english10k.data.repository.WordRepository;
import com.faridroid.english10k.service.UserProgressService;
import com.faridroid.english10k.service.WordService;

import java.util.List;
import java.util.stream.Collectors;

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

    public LiveData<List<WordInterface>> getWordsByLimit(int limit, String userId) {
        LiveData<List<WordDTO>> wordsByLimit = wordService.getWordsByLimit(limit);
        LiveData<List<UserProgressDTO>> allUserProgress = userProgressService.getAllUserProgressByType(userId, ProgressType.WORD_LEARNED);

        //if the word on wordsByLimit is already learned, remove it from the list

        return Transformations.switchMap(allUserProgress, userProgressList ->
                Transformations.map(wordsByLimit, words ->
                        words.stream()
                                .filter(word -> userProgressList.stream().noneMatch(progress -> String.valueOf(progress.getWordId()).equalsIgnoreCase(word.getId())))
                                .collect(Collectors.toList())
                )
        );
    }

    //todo: here check if needed to exclude the one already learned
    public LiveData<Integer> getTotalWords() {
        return repository.getTotalWords();
    }
}

