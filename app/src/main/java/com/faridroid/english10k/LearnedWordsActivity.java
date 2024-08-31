package com.faridroid.english10k;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.faridroid.english10k.service.UserProgressService;
import com.faridroid.english10k.view.adapter.LearnedWordsAdapter;
import com.faridroid.english10k.view.adapter.OnLearnedWordClickListener;
import com.faridroid.english10k.viewmodel.dto.ProgressType;
import com.faridroid.english10k.viewmodel.dto.UserDTO;
import com.faridroid.english10k.viewmodel.dto.UserProgressWordJoinDTO;

import java.util.List;

public class LearnedWordsActivity extends AppCompatActivity implements OnLearnedWordClickListener {

    private RecyclerView recyclerView;
    private LearnedWordsAdapter learnedWordsAdapter;
    private UserProgressService userProgressService;
    private UserDTO user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learned_words);

        //Read the extras to get the user id
        Bundle extras = getIntent().getExtras();
        this.user = (UserDTO) getIntent().getSerializableExtra("user");

        ProgressType type = extras != null && extras.containsKey("progressType")
                ? (ProgressType) extras.get("progressType")
                : ProgressType.WORD_LEARNED;

        recyclerView = findViewById(R.id.recyclerViewLearnedWords);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));
        userProgressService = UserProgressService.getInstance(getApplication());

        // Observa los datos
        userProgressService.listUserProgressWithWord(this.user.getId(), type)
                .observe(this, new Observer<List<UserProgressWordJoinDTO>>() {
                    @Override
                    public void onChanged(List<UserProgressWordJoinDTO> userProgressWordJoinDTOS) {
                        // Configura el adaptador cuando los datos cambian
                        learnedWordsAdapter = new LearnedWordsAdapter(userProgressWordJoinDTOS, LearnedWordsActivity.this);
                        recyclerView.setAdapter(learnedWordsAdapter);
                    }
                });
    }

    @Override
    public void onUnmarkClick(int userProgressId) {
        userProgressService.deleteUserProgress(userProgressId);
    }
}
