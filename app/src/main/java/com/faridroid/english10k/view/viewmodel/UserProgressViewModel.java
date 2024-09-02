package com.faridroid.english10k.view.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.faridroid.english10k.data.entity.UserProgress;
import com.faridroid.english10k.data.repository.UserProgressRepository;

import java.util.List;

public class UserProgressViewModel extends AndroidViewModel {
    private UserProgressRepository repository;

    public UserProgressViewModel(Application application) {
        super(application);
        repository = UserProgressRepository.getInstance(application);
    }

    //get all userProgress by userId
    public LiveData<List<UserProgress>> getAllUserProgress(String userId) {
        return repository.getAllUserProgress(userId);
    }


    
}
