package com.faridroid.english10k.service;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.faridroid.english10k.data.entity.UserProgress;
import com.faridroid.english10k.data.repository.UserProgressRepository;
import com.faridroid.english10k.viewmodel.dto.ProgressType;
import com.faridroid.english10k.viewmodel.dto.UserProgressDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UserProgressService {

    private UserProgressRepository userProgressRepository;
    private static UserProgressService instance;

    public static UserProgressService getInstance(Application application) {
        if (instance == null) {
            instance = new UserProgressService(application);
        }
        return instance;
    }

    public UserProgressService(Application application) {
        userProgressRepository = UserProgressRepository.getInstance(application);
    }

    public void deleteUserProgress(int id) {
        userProgressRepository.deleteUserProgress(id);
    }

    public LiveData<List<UserProgressDTO>> getAllUserProgress(String userId) {
        LiveData<List<UserProgress>> allUserProgress = userProgressRepository.getAllUserProgressByType(userId, ProgressType.WORD_LEARNED);

        return Transformations.map(allUserProgress, userProgressList -> {
            List<UserProgressDTO> dtoList = new ArrayList<>();
            for (UserProgress up : userProgressList) {
                UserProgressDTO dto = new UserProgressDTO(
                        up.getId(),
                        up.getWordId(),
                        up.getUserId(),
                        up.getStatus(),
                        up.getLastUpdated(),
                        up.getProgressType()
                );
                dtoList.add(dto);
            }
            return dtoList;
        });
    }

    public void insertUserProgress(UserProgressDTO userProgress) {
        UserProgress userProgressEntity = new UserProgress(
                userProgress.getId(),
                userProgress.getWordId(),
                userProgress.getUserId(),
                userProgress.getStatus(),
                userProgress.getLastUpdated(),
                userProgress.getProgressType()
        );
        userProgressRepository.insertUserProgress(userProgressEntity);
    }

}
