package com.faridroid.english10k.service;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.faridroid.english10k.data.dto.ProgressType;
import com.faridroid.english10k.data.dto.UserCustomProgressDTO;
import com.faridroid.english10k.data.dto.UserCustomProgressWordJoinDTO;
import com.faridroid.english10k.data.entity.UserCustomProgress;
import com.faridroid.english10k.data.repository.UserCustomProgressRepository;

import java.util.ArrayList;
import java.util.List;

public class UserCustomProgressService {

    private UserCustomProgressRepository userCustomProgressRepository;
    private static UserCustomProgressService instance;

    public static UserCustomProgressService getInstance(Application application) {
        if (instance == null) {
            instance = new UserCustomProgressService(application);
        }
        return instance;
    }

    private UserCustomProgressService(Application application) {
        userCustomProgressRepository = UserCustomProgressRepository.getInstance(application);
    }

    public void deleteUserProgress(String id) {
        userCustomProgressRepository.deleteUserCustomProgress(id);
    }

    public LiveData<List<UserCustomProgressDTO>> getAllUserProgressByType(String userId , ProgressType progressType) {
        LiveData<List<UserCustomProgress>> allUserProgress = userCustomProgressRepository.getAllUserCustomProgressByCustomWordId(userId, progressType);

        return Transformations.map(allUserProgress, userProgressList -> {
            List<UserCustomProgressDTO> dtoList = new ArrayList<>();
            for (UserCustomProgress up : userProgressList) {
                UserCustomProgressDTO dto = new UserCustomProgressDTO(
                        up.getId(),
                        up.getCustomWordId(),
                        up.getProgress(),
                        up.getProgressType()
                );
                dtoList.add(dto);
            }
            return dtoList;
        });
    }

    public void insertUserProgress(UserCustomProgress userProgress) {
        UserCustomProgress userProgressEntity = new UserCustomProgress(
                userProgress.getId(),
                userProgress.getCustomWordId(),
                userProgress.getProgress(),
                userProgress.getLastUpdated(),
                userProgress.getProgressType()
        );
        userCustomProgressRepository.insertUserCustomProgress(userProgressEntity);
    }

    public LiveData<List<UserCustomProgressWordJoinDTO>> listCustomUserProgressWithWordByListId(String userId,String listId, ProgressType progressType) {
        return  userCustomProgressRepository.listCustomUserProgressWithWordByListId(listId, userId,progressType);
    }

}
