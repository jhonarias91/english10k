package com.faridroid.english10k.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faridroid.english10k.R;
import com.faridroid.english10k.viewmodel.dto.WordDTO;

import java.util.List;

public class WordDTOAdapter extends RecyclerView.Adapter<WordDTOAdapter.WordDTOViewHolder> {

    private List<WordDTO> wordDTOList;

    public WordDTOAdapter(List<WordDTO> wordDTOList) {
        this.wordDTOList = wordDTOList;
    }

    @NonNull
    @Override
    public WordDTOViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word_pair, parent, false);
        return new WordDTOViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordDTOViewHolder holder, int position) {
        WordDTO wordDTO = wordDTOList.get(position);
        holder.textViewWord.setText(wordDTO.getWord());
        holder.textViewTranslation.setText(wordDTO.getSpanish());
    }

    @Override
    public int getItemCount() {
        return wordDTOList.size();
    }

    public void addWord(WordDTO wordDTO) {
        wordDTOList.add(wordDTO);
        notifyItemInserted(wordDTOList.size() - 1);
    }

    public static class WordDTOViewHolder extends RecyclerView.ViewHolder {
        TextView textViewWord;
        TextView textViewTranslation;

        public WordDTOViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewWord = itemView.findViewById(R.id.textViewWord);
            textViewTranslation = itemView.findViewById(R.id.textViewTranslation);
        }
    }
}
