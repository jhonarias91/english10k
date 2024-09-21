package com.faridroid.english10k.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faridroid.english10k.R;
import com.faridroid.english10k.data.dto.interfaces.WordInterface;
import com.faridroid.english10k.view.viewmodel.intf.OnLearnForgetCustomWordListener;

import java.util.List;

public class CustomWordAdapterOnPractice extends RecyclerView.Adapter<CustomWordAdapterOnPractice.CustomWordViewHolder> {
    private List<WordInterface> customWordList;
    private OnLearnForgetCustomWordListener onLearnForgetCustomWordListener;

    public CustomWordAdapterOnPractice(List<WordInterface> customWordList, OnLearnForgetCustomWordListener onLearnForgetCustomWordListener) {
        this.customWordList = customWordList;
        this.onLearnForgetCustomWordListener = onLearnForgetCustomWordListener;
    }

    @NonNull
    @Override
    public CustomWordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_word_practice, parent, false);
        return new CustomWordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomWordViewHolder holder, int position) {
        WordInterface customWordDTO = customWordList.get(position);
        holder.bind(customWordDTO);

        holder.switchLearned.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (onLearnForgetCustomWordListener != null) {
                    onLearnForgetCustomWordListener.onLearnCustomWord(customWordDTO.getId());
                }
            } else {
                if (onLearnForgetCustomWordListener != null) {
                    onLearnForgetCustomWordListener.onForgetCustomWord(customWordDTO.getId());
                }
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
        TextView statusTextView;
        Switch switchLearned;


        CustomWordViewHolder(View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.textViewCustomWord);
            translationTextView = itemView.findViewById(R.id.textViewTranslationCustomWord);
            deleteButton = itemView.findViewById(R.id.buttonLearnForget);
            switchLearned = itemView.findViewById(R.id.switchLearned);
            statusTextView = itemView.findViewById(R.id.statusTextView);
        }

        void bind(WordInterface customWord) {
            wordTextView.setText(customWord.getWord());
            translationTextView.setText(customWord.getSpanish());
            if (customWord.isLearned()) {
                statusTextView.setText("Aprendida");
                switchLearned.setChecked(true);
            } else {
                statusTextView.setText("Por aprender");
                switchLearned.setChecked(false);
            }

            // Listener para cambiar el estado
            switchLearned.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    statusTextView.setText("Aprendida");
                    customWord.setLearned(true);  // Actualizar el estado de la palabra en tu modelo de datos
                } else {
                    statusTextView.setText("No Aprendida");
                    customWord.setLearned(false);
                }
            });
        }
    }

    public List<WordInterface> getCustomWordList() {
        return customWordList;
    }

    public void setCustomWordList(List<WordInterface> customWordList) {
        this.customWordList = customWordList;
    }
        public interface OnCustomWordDeleteListener {
        void onDelete(String wordId, int position);
    }
}
