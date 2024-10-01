package com.faridroid.english10k.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.faridroid.english10k.R;
import com.faridroid.english10k.data.dto.interfaces.WordInterface;
import com.faridroid.english10k.view.viewmodel.intf.OnLearnForgetCustomWordListener;

import java.util.ArrayList;
import java.util.List;

public class CustomWordAdapterOnPractice extends RecyclerView.Adapter<CustomWordAdapterOnPractice.CustomWordViewHolder> {
    private List<WordInterface> fullWordList;      // Lista completa
    private List<WordInterface> filteredList;      // Lista filtrada
    private OnLearnForgetCustomWordListener onLearnForgetCustomWordListener;
    private OnLearnedWordClickListener listener;

    // Bandera para controlar si mostrar solo palabras no aprendidas
    private boolean showOnlyNotLearned = false;

    public CustomWordAdapterOnPractice(List<WordInterface> customWordList,
                                       OnLearnForgetCustomWordListener onLearnForgetCustomWordListener,
                                       OnLearnedWordClickListener listener) {
        this.fullWordList = new ArrayList<>(customWordList); // Copia completa
        this.filteredList = new ArrayList<>(customWordList); // Copia para mostrar
        this.onLearnForgetCustomWordListener = onLearnForgetCustomWordListener;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomWordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_word_practice, parent, false);
        return new CustomWordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomWordViewHolder holder, int position) {
        WordInterface customWordDTO = filteredList.get(position);
        holder.bind(customWordDTO);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    /**
     * Método para eliminar una palabra de la lista.
     * @param position Posición en la lista filtrada.
     */
    public void removeWord(int position) {
        WordInterface wordToRemove = filteredList.get(position);
        fullWordList.remove(wordToRemove);
        filteredList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Método para establecer el filtro basado en el estado booleano.
     * @param showOnlyNotLearned Si es true, muestra solo palabras no aprendidas.
     */
    public void setShowOnlyNotLearned(boolean showOnlyNotLearned) {
        this.showOnlyNotLearned = showOnlyNotLearned;
        applyFilter();
    }

    /**
     * Aplica el filtro basado en la bandera `showOnlyNotLearned`.
     */
    private void applyFilter() {
        if (showOnlyNotLearned) {
            // Filtra solo palabras que no han sido aprendidas
            filteredList = new ArrayList<>();
            for (WordInterface word : fullWordList) {
                if (!word.isLearned()) {
                    filteredList.add(word);
                }
            }
        } else {
            // Muestra todas las palabras
            filteredList = new ArrayList<>(fullWordList);
        }
        notifyDataSetChanged();
        listener.onFilterResults(filteredList.size());
    }


    public void setWordList(List<WordInterface> customWordList) {
        this.fullWordList = new ArrayList<>(customWordList);
        applyFilter(); // Reaplica el filtro con la nueva lista
    }

    /**
     * ViewHolder para cada elemento de la lista.
     */
    class CustomWordViewHolder extends RecyclerView.ViewHolder {
        TextView wordTextView;
        TextView translationTextView;
        Button deleteButton;
        SwitchCompat switchLearned;

        CustomWordViewHolder(View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.textViewCustomWord);
            translationTextView = itemView.findViewById(R.id.textViewTranslationCustomWord);
            deleteButton = itemView.findViewById(R.id.buttonLearnForget);
            switchLearned = itemView.findViewById(R.id.switchLearned);
        }

        void bind(WordInterface customWord) {
            wordTextView.setText(customWord.getWord());
            translationTextView.setText(customWord.getSpanish());

            // Elimina el listener antes de cambiar el estado del switch para evitar llamadas no deseadas
            switchLearned.setOnCheckedChangeListener(null);
            switchLearned.setChecked(customWord.isLearned());

            // Agrega nuevamente el listener
            switchLearned.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    customWord.setLearned(true);
                    if (onLearnForgetCustomWordListener != null) {
                        onLearnForgetCustomWordListener.onLearnCustomWord(customWord.getId());
                    }
                } else {
                    customWord.setLearned(false);
                    if (onLearnForgetCustomWordListener != null) {
                        onLearnForgetCustomWordListener.onForgetCustomWord(customWord.getId());
                    }
                }
                // Si el filtro está activo, actualizar la lista tras el cambio
                if (showOnlyNotLearned) {
                    applyFilter();
                }
            });

            // Configura el botón de eliminar si es necesario
            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    removeWord(position);
                    if (listener != null) {
                        listener.onFilterResults(filteredList.size());
                    }
                }
            });
        }
    }

    /**
     * Interfaz para manejar la eliminación de palabras.
     */
    public interface OnCustomWordDeleteListener {
        void onDelete(String wordId, int position);
    }

}
