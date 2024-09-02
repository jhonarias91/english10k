package com.faridroid.english10k.service;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.faridroid.english10k.data.entity.UserProgress;
import com.faridroid.english10k.data.repository.UserProgressRepository;
import com.faridroid.english10k.data.dto.ProgressType;
import com.faridroid.english10k.data.dto.UserProgressDTO;
import com.faridroid.english10k.data.dto.UserProgressWordJoinDTO;

import java.util.ArrayList;
import java.util.List;

public class UserProgressService {

    private UserProgressRepository userProgressRepository;
    private static UserProgressService instance;

    public static UserProgressService getInstance(Application application) {
        if (instance == null) {
            instance = new UserProgressService(application);
        }
        return instance;
    }

    private UserProgressService(Application application) {
        userProgressRepository = UserProgressRepository.getInstance(application);
    }

    public void deleteUserProgress(int id) {
        userProgressRepository.deleteUserProgress(id);
    }

    public LiveData<List<UserProgressDTO>> getAllUserProgressByType(String userId , ProgressType progressType) {
        LiveData<List<UserProgress>> allUserProgress = userProgressRepository.getAllUserProgressByType(userId,progressType);

        return Transformations.map(allUserProgress, userProgressList -> {
            List<UserProgressDTO> dtoList = new ArrayList<>();
            for (UserProgress up : userProgressList) {
                UserProgressDTO dto = new UserProgressDTO(
                        up.getWordId(),
                        up.getWordId(),
                        up.getUserId(),
                        up.getStatus(),
                        up.getUpdated(),
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

    public LiveData<List<UserProgressWordJoinDTO>> listUserProgressWithWord(String userId, ProgressType progressType) {
        return  userProgressRepository.listUserProgressWithWord(userId,progressType);
    }

}
