package com.faridroid.english10k.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faridroid.english10k.R;
import com.faridroid.english10k.viewmodel.dto.UserProgressWordJoinDTO;

import java.util.List;

public class LearnedWordsAdapter extends RecyclerView.Adapter<LearnedWordsAdapter.WordViewHolder> {

    private List<UserProgressWordJoinDTO> learnedWords;
    private OnLearnedWordClickListener listener;

    public LearnedWordsAdapter(List<UserProgressWordJoinDTO> learnedWords, OnLearnedWordClickListener listener) {
        this.learnedWords = learnedWords;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(com.faridroid.english10k.R.layout.item_learned_word, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        UserProgressWordJoinDTO word = learnedWords.get(position);
        holder.bind(word, listener);
    }

    @Override
    public int getItemCount() {
        return learnedWords.size();
    }

    public static class WordViewHolder extends RecyclerView.ViewHolder {

        TextView textViewWord;
        TextView textViewTranslation;
        Button buttonUnmark;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewWord = itemView.findViewById(R.id.textViewWord);
            textViewTranslation = itemView.findViewById(R.id.textViewTranslation);
            buttonUnmark = itemView.findViewById(R.id.buttonUnmark);
        }

        public void bind(final UserProgressWordJoinDTO wordWithProgress, final OnLearnedWordClickListener listener) {
            textViewWord.setText(wordWithProgress.getWord().getWord());
            textViewTranslation.setText(wordWithProgress.getWord().getSpanish());

            buttonUnmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onUnmarkClick(wordWithProgress.getUserProgress().getUserProgressId());
                }
            });
        }
    }
}
