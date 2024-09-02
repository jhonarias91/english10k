package com.faridroid.english10k.view.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.faridroid.english10k.data.dto.UserDTO;

public class FlashcardsSettingsViewModel extends AndroidViewModel {

    private final WordViewModel wordViewModel;
    private final SharedPreferences preferences;
    private UserDTO user;

    public FlashcardsSettingsViewModel(Application application, WordViewModel wordViewModel, SharedPreferences sharedPreferences, UserDTO user) {
        super(application);
        this.wordViewModel = wordViewModel;
        this.preferences = sharedPreferences;
        this.user = user;
    }

    public LiveData<SeekBarRange> getRange() {
        return Transformations.map(wordViewModel.getTotalWords(), totalDbWords -> {
            if (totalDbWords != null) {
                int min = 5;
                int max = Math.min((int) Math.min(totalDbWords,this.user.getXp() + 100), totalDbWords);
                int savedProgress = preferences.getInt("flashcards_range", -1);

                int progress = (savedProgress != -1) ? savedProgress : max / 2;

                return new SeekBarRange(min, progress, max);
            }
            return new SeekBarRange(0, 0, 0);
        });
    }

    public void setRange(int wordsToPlay) {
        preferences.edit().putInt("flashcards_range", wordsToPlay).apply();
    }
}
