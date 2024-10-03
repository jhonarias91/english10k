package com.faridroid.english10k.view.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.faridroid.english10k.data.dto.CustomWordDTO;
import com.faridroid.english10k.data.dto.ProgressType;
import com.faridroid.english10k.data.dto.UserCustomProgressWordJoinDTO;
import com.faridroid.english10k.data.dto.interfaces.WordInterface;
import com.faridroid.english10k.data.entity.CustomWord;
import com.faridroid.english10k.data.enums.WordsGameTypeEnum;
import com.faridroid.english10k.service.CustomWordService;
import com.faridroid.english10k.service.UserCustomProgressService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomWordViewModel extends AndroidViewModel {

    private CustomWordService customWordService;
    private UserCustomProgressService userCustomProgressService;

    public CustomWordViewModel(Application application) {
        super(application);
        customWordService = CustomWordService.getInstance(application);
        userCustomProgressService = UserCustomProgressService.getInstance(application);
    }

    public LiveData<List<WordInterface>> getCustomWordsByList(String listId) {
        return customWordService.getCustomWordsByList(listId);
    }

    public LiveData<List<WordInterface>> getAllWordsWithLearnedMark(String userId, String listId) {

        LiveData<List<WordInterface>> customWordsByList = customWordService.getCustomWordsByList(listId);
        LiveData<List<UserCustomProgressWordJoinDTO>> learnedWordsLiveData = userCustomProgressService.listCustomUserProgressWithWordByListId(listId, ProgressType.WORD_LEARNED);

        return Transformations.switchMap(learnedWordsLiveData, learnedWords ->
                Transformations.map(customWordsByList, customWords -> {
                    Set<String> learnedWordIds;

                    if (learnedWords != null) {
                        learnedWordIds = learnedWords.stream()
                                .map(dto -> dto.getWord().getId())
                                .collect(Collectors.toSet());
                    } else {
                        learnedWordIds = new HashSet<>();
                    }

                    return customWords.stream()
                            .map(word -> {
                                boolean isLearned = learnedWordIds.contains(word.getId());
                                return new CustomWordDTO(word.getId(), word.getWord(), word.getSpanish(), isLearned);
                            })
                            .collect(Collectors.toList());
                })
        );
    }

    public void insertCustomWord(CustomWordDTO customWordDTO) {
        customWordService.insertCustomWord(customWordDTO);
    }

    public void updateCustomWord(CustomWordDTO customWordDTO) {
        customWordService.updateCustomWord(customWordDTO);
    }

    public void deleteCustomWord(String id) {
        customWordService.deleteCustomWord(id);
    }

    private CustomWord mapDTOToEntity(CustomWordDTO dto) {
        return new CustomWord(dto.getId(), dto.getListId(), dto.getWord(), dto.getSpanish());
    }

    private CustomWordDTO mapEntityToDTO(CustomWord entity) {
        return new CustomWordDTO(entity.getId(), entity.getListId(), entity.getWord(), entity.getSpanish());
    }

    public LiveData<List<WordInterface>> getNonLearnedCustomWordsByList(String userId, String listId, WordsGameTypeEnum wordsGameTypeEnum) {

        LiveData<List<WordInterface>> customWordsByList = customWordService.getCustomWordsByList(listId);
        LiveData<List<UserCustomProgressWordJoinDTO>> learnedWordsLiveData = userCustomProgressService.listCustomUserProgressWithWordByListId(listId, ProgressType.WORD_LEARNED);

        // Filtrar las palabras aprendidas utilizando Transformations
        return Transformations.switchMap(learnedWordsLiveData, learnedWords ->
                Transformations.map(customWordsByList, customWords ->
                        customWords.stream()
                                .filter(word -> learnedWords.stream()
                                        .noneMatch(progress -> progress.getWord().getId().equalsIgnoreCase(word.getId())))
                                .collect(Collectors.toList())
                )
        );
    }

    public LiveData<List<WordInterface>> getCustomWordsByListId(String listId, WordsGameTypeEnum type) {
        if (type.equals(WordsGameTypeEnum.TO_LEARN)) {
            LiveData<List<WordInterface>> wordsNotLearned = customWordService.getWordsNotLearned(listId, ProgressType.WORD_LEARNED);
            return Transformations.map(wordsNotLearned, words -> new ArrayList<>(words));
        } else if (type.equals(WordsGameTypeEnum.LEARNED)) {
            LiveData<List<WordInterface>> wordsLearned = customWordService.getWordsLearned(listId, ProgressType.WORD_LEARNED);
            return Transformations.map(wordsLearned, words -> new ArrayList<>(words));
        } else {
            return new MutableLiveData<>(new ArrayList<>());
        }
    }

    public void deleteCustomList(String currentListId) {
        customWordService.deleteCustomList(currentListId);
    }
}
