package com.faridroid.english10k.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faridroid.english10k.R;
import com.faridroid.english10k.data.entity.Word;
import com.faridroid.english10k.data.dto.UserProgressWordJoinDTO;

import java.util.ArrayList;
import java.util.List;

public class LearnedWordsAdapter extends RecyclerView.Adapter<LearnedWordsAdapter.WordViewHolder> implements Filterable {

    private List<UserProgressWordJoinDTO> learnedWords;
    private List<UserProgressWordJoinDTO> filteredList;
    private OnLearnedWordClickListener listener;

    public LearnedWordsAdapter(List<UserProgressWordJoinDTO> learnedWords, OnLearnedWordClickListener listener) {
        this.learnedWords = learnedWords;
        this.filteredList = new ArrayList<>(learnedWords);
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
        UserProgressWordJoinDTO word = filteredList.get(position);
        holder.bind(word, listener);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase().trim();
                List<UserProgressWordJoinDTO> filtered = new ArrayList<>();

                if (query.isEmpty()) {
                    filtered = learnedWords;
                } else {
                    for (UserProgressWordJoinDTO item : learnedWords) {
                        final Word word = item.getWord();
                        if (word.getWord().toLowerCase().contains(query) || word.getSpanish().toLowerCase().contains(query)) {
                            filtered.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (List<UserProgressWordJoinDTO>) results.values;
                notifyDataSetChanged();
                listener.onFilterResults(filteredList.size());
            }
        };
    }

    public void filter(String query) {
        getFilter().filter(query);
    }

    public static class WordViewHolder extends RecyclerView.ViewHolder {

        TextView textViewWord;
        TextView textViewTranslation;
        Button buttonUnmark;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewWord = itemView.findViewById(R.id.textViewCustomWord);
            textViewTranslation = itemView.findViewById(R.id.textViewTranslationCustomWord);
            buttonUnmark = itemView.findViewById(R.id.buttonDeleteCustomWord);
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
