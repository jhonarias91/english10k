package com.faridroid.english10k.view.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.faridroid.english10k.data.dto.ProgressType;
import com.faridroid.english10k.data.entity.UserCustomProgress;
import com.faridroid.english10k.service.UserCustomProgressService;

import java.util.UUID;

public class CustomUserProgressViewModel extends AndroidViewModel {
    private UserCustomProgressService userCustomProgressService;

    public CustomUserProgressViewModel(Application application) {
        super(application);
        userCustomProgressService = UserCustomProgressService.getInstance(application);
    }

    public void insertCustomUserProgress(String wordId) {
        long lastUpdated = System.currentTimeMillis();
        UserCustomProgress userProgressDTO = new UserCustomProgress(UUID.randomUUID().toString(), wordId,
                0, lastUpdated, ProgressType.WORD_LEARNED);
        userCustomProgressService.insertUserProgress(userProgressDTO);
    }

    public void deleteLearnedCustomUserProgressByWordId(String wordId) {
        userCustomProgressService.deleteUserCustomProgressByWordIdAndProgressType(wordId, ProgressType.WORD_LEARNED);
    }
}
