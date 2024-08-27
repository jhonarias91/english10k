package com.faridroid.english10k.viewmodel.factory;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.faridroid.english10k.viewmodel.FlashcardsSettingsViewModel;
import com.faridroid.english10k.viewmodel.WordViewModel;
import com.faridroid.english10k.viewmodel.dto.UserDTO;

public class FlashcardsSettingsViewModelFactory implements ViewModelProvider.Factory {

    private final Application application;
    private final WordViewModel wordViewModel;
    private final SharedPreferences preferences;
    private UserDTO user;

    public FlashcardsSettingsViewModelFactory(Application application, WordViewModel wordViewModel, SharedPreferences preferences,  UserDTO user) {
        this.application = application;
        this.wordViewModel = wordViewModel;
        this.preferences = preferences;
        this.user = user;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FlashcardsSettingsViewModel.class)) {
            return (T) new FlashcardsSettingsViewModel(application, wordViewModel, preferences, user);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
