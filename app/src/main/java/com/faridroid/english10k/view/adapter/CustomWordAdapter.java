package com.faridroid.english10k.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faridroid.english10k.R;
import com.faridroid.english10k.data.dto.CustomWordDTO;

import java.util.List;

public class CustomWordAdapter extends RecyclerView.Adapter<CustomWordAdapter.CustomWordViewHolder> {
    private List<CustomWordDTO> customWordList;
    private OnCustomWordDeleteListener onCustomWordDeleteListener;

    public CustomWordAdapter(List<CustomWordDTO> customWordList, OnCustomWordDeleteListener onCustomWordDeleteListener) {
        this.customWordList = customWordList;
        this.onCustomWordDeleteListener = onCustomWordDeleteListener;
    }

    @NonNull
    @Override
    public CustomWordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_word, parent, false);
        return new CustomWordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomWordViewHolder holder, int position) {
        CustomWordDTO customWordDTO = customWordList.get(position);
        holder.bind(customWordDTO);

        holder.deleteButton.setOnClickListener(v -> {
            if (onCustomWordDeleteListener != null) {
                onCustomWordDeleteListener.onDelete(customWordDTO.getId(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return customWordList.size();
    }

    public void removeWord(int position) {
        customWordList.remove(position);
        notifyItemRemoved(position);
    }

    class CustomWordViewHolder extends RecyclerView.ViewHolder {
        TextView wordTextView;
        TextView translationTextView;
        Button deleteButton;

        CustomWordViewHolder(View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.textViewCustomWord);
            translationTextView = itemView.findViewById(R.id.textViewTranslationCustomWord);
            deleteButton = itemView.findViewById(R.id.buttonDeleteCustomWord);
        }

        void bind(CustomWordDTO customWordDTO) {
            wordTextView.setText(customWordDTO.getWord());
            translationTextView.setText(customWordDTO.getSpanish());
        }
    }

    public interface OnCustomWordDeleteListener {
        void onDelete(String wordId, int position);
    }
}
