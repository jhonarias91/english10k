package com.faridroid.english10k.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.faridroid.english10k.service.UserProgressService;
import com.faridroid.english10k.viewmodel.dto.ProgressType;
import com.faridroid.english10k.viewmodel.dto.UserProgressDTO;

import java.util.List;

public class LearnedWordsViewmodel extends AndroidViewModel {

    private UserProgressService userProgressService;

    public LearnedWordsViewmodel(@NonNull Application application) {
        super(application);

        userProgressService = UserProgressService.getInstance(application);
    }

    public void deleteUserProgress(int id) {
        userProgressService.deleteUserProgress(id);
    }
    public LiveData<List<UserProgressDTO>> getAllUserProgress(String userId) {
        return userProgressService.getAllUserProgressByType(userId,  ProgressType.WORD_LEARNED);
    }





}
